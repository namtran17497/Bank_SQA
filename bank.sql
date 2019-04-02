-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 02, 2019 lúc 09:05 PM
-- Phiên bản máy phục vụ: 10.1.38-MariaDB
-- Phiên bản PHP: 7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `bank`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer`
--

CREATE TABLE `customer` (
  `id` int(5) NOT NULL,
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci NOT NULL,
  `info` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `customer`
--

INSERT INTO `customer` (`id`, `name`, `info`) VALUES
(1, 'Nguyen Van B', '213546841'),
(2, 'Tran Dai Nam', '546894521'),
(3, 'Nguyen Van A', '234561974');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loan`
--

CREATE TABLE `loan` (
  `id` int(4) NOT NULL,
  `amount` bigint(100) NOT NULL,
  `start_date` date NOT NULL,
  `term` varchar(30) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci NOT NULL,
  `interest` varchar(5) NOT NULL,
  `idCustomer` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `loan`
--

INSERT INTO `loan` (`id`, `amount`, `start_date`, `term`, `interest`, `idCustomer`) VALUES
(1, 2000000, '2019-02-08', '6 tháng', '6.50', 1),
(2, 20000000, '2019-03-15', '12 tháng', '7.00', 3),
(3, 5000000, '2019-03-28', '1 tháng', '6.00', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loan_interest`
--

CREATE TABLE `loan_interest` (
  `id` int(4) NOT NULL,
  `term` varchar(30) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci NOT NULL,
  `amount` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `interest` varchar(5) NOT NULL,
  `version` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `loan_interest`
--

INSERT INTO `loan_interest` (`id`, `term`, `amount`, `interest`, `version`) VALUES
(184, '01 tháng', '<100 triệu', '6.10', '28.03.2019_21:27:18'),
(185, '03 tháng', '<100 triệu', '6.50', '28.03.2019_21:27:18'),
(186, '04 tháng', '100 triệu - <250 triệu', '8.20', '28.03.2019_21:27:18'),
(187, '06 tháng', '<100 triệu', '8.50', '28.03.2019_21:27:18'),
(188, '12 tháng', '250 triệu - <500 triệu', '5.50', '28.03.2019_21:27:18'),
(189, '01 tháng', '<100 triệu', '6.10', '28.03.2019_21:28:55'),
(190, '03 tháng', '<100 triệu', '6.50', '28.03.2019_21:28:55'),
(191, '04 tháng', '100 triệu - <250 triệu', '8.20', '28.03.2019_21:28:55'),
(192, '06 tháng', '<100 triệu', '8.50', '28.03.2019_21:28:55'),
(193, '12 tháng', '250 triệu - <500 triệu', '5.50', '28.03.2019_21:28:55'),
(194, '01 tháng', '<100 triệu', '6.10', '28.03.2019_21:32:42'),
(195, '03 tháng', '<100 triệu', '6.50', '28.03.2019_21:32:42'),
(196, '04 tháng', '100 triệu - <250 triệu', '8.20', '28.03.2019_21:32:42'),
(197, '06 tháng', '<100 triệu', '8.50', '28.03.2019_21:32:42'),
(198, '12 tháng', '250 triệu - <500 triệu', '5.50', '28.03.2019_21:32:42');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `saving`
--

CREATE TABLE `saving` (
  `id` int(4) NOT NULL,
  `amount` bigint(100) NOT NULL,
  `start_date` varchar(10) NOT NULL,
  `term` varchar(30) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci NOT NULL,
  `interest` varchar(5) NOT NULL,
  `idCustomer` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `saving`
--

INSERT INTO `saving` (`id`, `amount`, `start_date`, `term`, `interest`, `idCustomer`) VALUES
(1, 1000000000, '2018-04-17', '12 tháng', '7.70', 1),
(2, 20000000, '2018-09-12', '6 tháng', '7.60', 2),
(3, 10000000000, '2018-09-25', '12 tháng', '7.80', 3);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `saving_interest`
--

CREATE TABLE `saving_interest` (
  `id` int(4) NOT NULL,
  `term` varchar(30) CHARACTER SET utf8 COLLATE utf8_vietnamese_ci NOT NULL,
  `amount` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `interest` varchar(5) NOT NULL,
  `version` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `saving_interest`
--

INSERT INTO `saving_interest` (`id`, `term`, `amount`, `interest`, `version`) VALUES
(243, '01 tháng', '<100 triệu', '6.10', '28.03.2019_21:27:18'),
(244, '03 tháng', '<100 triệu', '6.50', '28.03.2019_21:27:18'),
(245, '03 tháng', '250 triệu - <500 triệu', '5.50', '28.03.2019_21:27:18'),
(246, '05 tháng', '<100 triệu', '5.60', '28.03.2019_21:27:18'),
(247, '36 tháng', '<100 triệu', '9.99', '28.03.2019_21:27:18'),
(248, 'Không kỳ hạn', '250 triệu - <500 triệu', '1.25', '28.03.2019_21:27:18'),
(249, '01 tháng', '<100 triệu', '6.10', '28.03.2019_21:28:55'),
(250, '03 tháng', '<100 triệu', '6.50', '28.03.2019_21:28:55'),
(251, '03 tháng', '250 triệu - <500 triệu', '5.50', '28.03.2019_21:28:55'),
(252, '05 tháng', '<100 triệu', '5.60', '28.03.2019_21:28:55'),
(253, '36 tháng', '<100 triệu', '9.99', '28.03.2019_21:28:55'),
(254, 'Không kỳ hạn', '250 triệu - <500 triệu', '1.25', '28.03.2019_21:28:55'),
(255, '01 tháng', '<100 triệu', '6.10', '28.03.2019_21:32:42'),
(256, '03 tháng', '<100 triệu', '6.50', '28.03.2019_21:32:42'),
(257, '03 tháng', '250 triệu - <500 triệu', '5.50', '28.03.2019_21:32:42'),
(258, '05 tháng', '<100 triệu', '5.60', '28.03.2019_21:32:42'),
(259, '36 tháng', '<100 triệu', '9.99', '28.03.2019_21:32:42'),
(260, 'Không kỳ hạn', '250 triệu - <500 triệu', '1.25', '28.03.2019_21:32:42');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id` int(3) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`id`, `username`, `password`) VALUES
(1, 'namtran', '12345678');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `info` (`info`);

--
-- Chỉ mục cho bảng `loan`
--
ALTER TABLE `loan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idCustomer` (`idCustomer`),
  ADD KEY `term` (`term`);

--
-- Chỉ mục cho bảng `loan_interest`
--
ALTER TABLE `loan_interest`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `saving`
--
ALTER TABLE `saving`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idCustomer` (`idCustomer`),
  ADD KEY `term` (`term`);

--
-- Chỉ mục cho bảng `saving_interest`
--
ALTER TABLE `saving_interest`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `loan`
--
ALTER TABLE `loan`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `loan_interest`
--
ALTER TABLE `loan_interest`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=199;

--
-- AUTO_INCREMENT cho bảng `saving`
--
ALTER TABLE `saving`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `saving_interest`
--
ALTER TABLE `saving_interest`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=261;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `loan`
--
ALTER TABLE `loan`
  ADD CONSTRAINT `loan_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`id`) ON DELETE NO ACTION;

--
-- Các ràng buộc cho bảng `saving`
--
ALTER TABLE `saving`
  ADD CONSTRAINT `saving_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`id`) ON DELETE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
