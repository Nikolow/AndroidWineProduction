-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 25 апр 2020 в 14:16
-- Версия на сървъра: 10.4.11-MariaDB
-- PHP Version: 7.2.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `test`
--

-- --------------------------------------------------------

--
-- Структура на таблица `bottles`
--

CREATE TABLE `bottles` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `type` int(11) NOT NULL,
  `ml` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Схема на данните от таблица `bottles`
--

INSERT INTO `bottles` (`id`, `name`, `type`, `ml`, `time`) VALUES
(1, 'niki_bottle', 0, 750, '2020-04-08 21:38:15'),
(2, 'krisi_bottle', 1, 750, '2020-03-28 18:16:44'),
(3, 'test1', 0, 375, '2020-03-27 18:16:44'),
(4, 'test2', 1, 200, '2020-03-26 18:16:44'),
(5, 'test3', 0, 187, '2020-03-25 18:16:44'),
(6, 'test4', 1, 187, '2020-04-04 11:19:18'),
(9, 'test5', 0, 750, '2020-04-07 19:47:08'),
(10, 'test6', 1, 375, '2020-04-07 19:47:24'),
(11, 'test7', 0, 200, '2020-04-07 19:47:36'),
(12, 'test8', 1, 187, '2020-04-22 12:33:29');

-- --------------------------------------------------------

--
-- Структура на таблица `bottling`
--

CREATE TABLE `bottling` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `wineid` int(11) NOT NULL,
  `bottleid` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Схема на данните от таблица `bottling`
--

INSERT INTO `bottling` (`id`, `name`, `wineid`, `bottleid`, `time`) VALUES
(11, 'Lipa2010 (Red lipa from NikolowGrapes) - 187ml Glass Bottle (test3)', 11, 5, '2020-04-04 12:15:16'),
(12, 'Merlo2020 (Red merlo from NikolowGrapes) - 187ml Glass Bottle (test3)', 10, 5, '2020-04-04 12:18:17'),
(14, 'Merlo2002 (Red merlo from NikolowGrapes) - 750ml Glass Bottle (niki_bottle)', 9, 1, '2020-04-05 21:10:16'),
(15, 'Merlo2002 (Red merlo from NikolowGrapes) - 187ml Plastic Bottle (test4)', 9, 6, '2020-04-05 21:11:38'),
(16, 'Lipa2010 (Red lipa from NikolowGrapes) - 187ml Plastic Bottle (test4)', 11, 6, '2020-04-05 21:13:00'),
(17, 'Merlo2002 (Red merlo from NikolowGrapes) - 187ml Plastic Bottle (test8)', 9, 12, '2020-04-07 20:20:49');

-- --------------------------------------------------------

--
-- Структура на таблица `grapes`
--

CREATE TABLE `grapes` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `type` int(11) NOT NULL,
  `producer` varchar(200) NOT NULL,
  `q` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Схема на данните от таблица `grapes`
--

INSERT INTO `grapes` (`id`, `name`, `type`, `producer`, `q`, `time`) VALUES
(1, 'lipa', 1, 'NikolowGrapes', 120, '2020-03-29 17:17:29'),
(2, 'lipa2', 1, 'NikolowGrapes', 80, '2020-03-28 18:17:29'),
(3, 'merlo', 1, 'NikolowGrapes', 150, '2020-04-04 11:19:39'),
(7, 'merlo2', 0, 'test', 123, '2020-04-07 19:56:37'),
(8, 'merlo3', 1, 'test', 333, '2020-04-07 19:56:50');

-- --------------------------------------------------------

--
-- Структура на таблица `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` text NOT NULL,
  `access` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Схема на данните от таблица `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `access`) VALUES
(1, 'admin', 'admin@android.lol', 'D033E22AE348AEB5660FC2140AEC35850C4DA997', 3),
(2, 'operator', 'operator@android.lol', 'FE96DD39756AC41B74283A9292652D366D73931F', 2),
(3, 'user', 'user@android.lol', '12DEA96FEC20593566AB75692C9949596833ADC9', 1),
(4, 'hgggg23', 'dsau21@abv.bg', 'FE96DD39756AC41B74283A9292652D366D73931F', 2),
(5, 'lqlq', 'lqlq@lqlq.lqlq', '7CAB9F8A757D190703995BDD77A1173291BE59F7', 1),
(6, 'xd', 'xd@xd.xd', '7782391677C36F0F0E77363C7EF182E4E75E7669', 1),
(7, 'xd2', 'xd2@xd.xd', '3C8512E0B5B330ACCB0039A4915AE7D88B691596', 1),
(8, 'xd4', 'xd4@xd4.xd4', '34B1176D457E3D88E92D4A76A5B4052F1718A40C', 1),
(9, 'xd3', 'xd3@xd3.xd3', '3ABCCB20AB9CC50D878EDD7EC24C926D3D21A651', 1),
(10, 'xd5', 'xd5@xd3.xd3', '5D10A0CAD780279B74403105C9E5F60665A8E492', 1),
(11, 'xd6', 'xd6@xd3.xd3', '7F6A1103EDA316830E11F7CA839B6CAC8BB88FBC', 1),
(12, 'xd7', 'xd7@xd3.xd3', 'C7E11E8E4D0AA7105C002B6B62D02E40F2D03A41', 1),
(13, 'xd8', 'xd8@xd7.xd7', 'D6F5250FC37FF3AA9AFB49A7A0CED2237109FB7F', 1),
(14, 'lqlq8', 'lqlq8@lqlq8.xd8', '9B80E412D5084016460D56B92BAD912AA1C8E12E', 1),
(15, 'lqlq9', 'lqlq9@lqlq8.xd8', 'B0A5101F59E0581B43A971A1719C4392D967AD83', 1),
(16, 'operator6', 'operator2', 'B7A98F2FB08110CBE6CEAB57302C55EB5A0AADB1', 2),
(17, 'addnew', 'pass@pass.pas', 'BB0ED08AB988D62CB9E41A01D7DBA97FF83565FF', 1),
(18, 'test15', 'test15@21.21', '61BB70FA60368F069E62D601C357D203700AB2D2', 1),
(19, 'test16', '321ds@ds2.2', '1FBEFEE9CFB86926757519357E077FD6A21AEF0F', 2),
(20, 'test17', '321@321.22', '08A25C0F270B29AEBA650E6B2D1A9947A778C5DA', 3),
(21, 'last_test', 'last@mail.bg', '847475AF91C9C0BBCBA9AB673493043B3303457B', 2),
(22, 'lqlq399', 'lqlqdd@lqlq.lqlq', '2585189EA0BE8C67EC074205C64696CC0011D470', 3),
(24, 'testUser', '1@1.com', 'FE5B6E22EB30CDAD08B2D6F62C722BA8E1F6CC15', 1),
(25, 'testOperator', 'testOperator', '203A93A00BACFA9A8FE4A694E9F969D2CE27CAA2', 2),
(27, 'test25', 'test25@abv.bg', '100EF754E0696AAAAD9CF2CDC63EC48EEEF54A7F', 1);

-- --------------------------------------------------------

--
-- Структура на таблица `wines`
--

CREATE TABLE `wines` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `grape` int(11) NOT NULL,
  `q` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Схема на данните от таблица `wines`
--

INSERT INTO `wines` (`id`, `name`, `grape`, `q`, `time`) VALUES
(9, 'Merlo2002', 3, 50, '2020-04-04 11:27:46'),
(10, 'Merlo2020', 3, 100, '2020-04-04 11:27:57'),
(11, 'Lipa2010', 1, 55, '2020-04-04 11:28:07'),
(14, '123', 3, 321, '2020-04-06 08:49:46');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bottles`
--
ALTER TABLE `bottles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bottling`
--
ALTER TABLE `bottling`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `grapes`
--
ALTER TABLE `grapes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `wines`
--
ALTER TABLE `wines`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bottles`
--
ALTER TABLE `bottles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `bottling`
--
ALTER TABLE `bottling`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `grapes`
--
ALTER TABLE `grapes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `wines`
--
ALTER TABLE `wines`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
