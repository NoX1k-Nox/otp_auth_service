package com.example.otp;

import com.example.otp.controller.OtpController;
import com.example.otp.controller.UserController;
import com.example.otp.dao.impl.OtpCodeDaoImpl;
import com.example.otp.dao.impl.UserDaoImpl;
import com.example.otp.dao.impl.OtpConfigurationDaoImpl;
import com.example.otp.notification.NotificationService;
import com.example.otp.notification.impl.EmailNotificationService;
import com.example.otp.notification.impl.TelegramNotificationService;
import com.example.otp.notification.impl.FileNotificationService;
import com.example.otp.service.OtpCodeService;
import com.example.otp.service.OtpConfigurationService;
import com.example.otp.service.UserService;
import com.example.otp.service.impl.OtpCodeServiceImpl;
import com.example.otp.service.impl.OtpConfigurationServiceImpl;
import com.example.otp.service.impl.UserServiceImpl;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OtpAuthApplication {

    public static void main(String[] args) {
        OtpCodeService otpCodeService;

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/otpdb", "user", "password"
            );

            UserDaoImpl userDao = new UserDaoImpl(connection);
            OtpCodeDaoImpl otpCodeDao = new OtpCodeDaoImpl(connection);
            OtpConfigurationDaoImpl otpConfigDao = new OtpConfigurationDaoImpl(connection);

            UserService userService = new UserServiceImpl(userDao);
            OtpConfigurationService otpConfigService = new OtpConfigurationServiceImpl(otpConfigDao);

            NotificationService notificationService = new NotificationService(List.of(
                    new EmailNotificationService(),
                    new TelegramNotificationService(),
                    new FileNotificationService()
            ));

            otpCodeService = new OtpCodeServiceImpl(
                    otpCodeDao,
                    otpConfigService,
                    notificationService
            );

            UserController userController = new UserController(userService);
            OtpController otpController = new OtpController(otpCodeService);

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.setExecutor(Executors.newFixedThreadPool(10));
            Router router = new Router(server, userController, otpController);
            router.registerRoutes();

            server.start();
            System.out.println("Server started on port 8080...");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.err.println("Failed to start the server due to: " + e.getMessage());
            return;
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
                otpCodeService::expireOldCodes,
                0,
                5, TimeUnit.MINUTES
        );
    }
}
