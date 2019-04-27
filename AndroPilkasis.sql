-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 28, 2019 at 09:57 AM
-- Server version: 5.7.25-0ubuntu0.18.10.2
-- PHP Version: 7.2.15-0ubuntu0.18.10.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `AndroPilkasis`
--

-- --------------------------------------------------------

--
-- Table structure for table `allowed_users`
--

CREATE TABLE `allowed_users` (
  `num_id` int(11) NOT NULL,
  `group_id` varchar(32) CHARACTER SET utf8 NOT NULL,
  `username` varchar(64) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `allowed_users`
--

INSERT INTO `allowed_users` (`num_id`, `group_id`, `username`) VALUES
(1, 'Jepurun', 'Alexz');

-- --------------------------------------------------------

--
-- Table structure for table `kandidat`
--

CREATE TABLE `kandidat` (
  `num_id` int(11) NOT NULL,
  `nama` varchar(64) CHARACTER SET utf8 NOT NULL,
  `kelas` varchar(24) CHARACTER SET utf8 NOT NULL,
  `visi` varchar(512) CHARACTER SET utf8 NOT NULL,
  `misi` varchar(1024) CHARACTER SET utf8 NOT NULL,
  `avatar` varchar(128) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kandidat`
--

INSERT INTO `kandidat` (`num_id`, `nama`, `kelas`, `visi`, `misi`, `avatar`) VALUES
(1, 'Horeph Subandono', 'kakap', 'Mewujudkan lingkungan yang bersih, aman, nyaman dan tenteram', '1. Mengadakan rutinitas kebersihan yang dilakukan setiap hari\r\n2. Membersihkan hati dan pikiran dari godaan yang jahat\r\n3. Mengadakan kegiatan pengajian sebelum pelajaran dimulai\r\n4. Mengoptimalkan Beberapa kegiatan sekolah', 'horeph_img.jpg'),
(2, 'Tigor Subekti', 'kakap', 'Meningkatkan nilai - nilai etika dan norma yang berlaku di Pancasila', '1. Mengadakan kegiatan Sosialisasi Pancasila oleh pihak nasionalis setiap bulannya\r\n2. Optimalisasi peraturan di sekolah agar sesuai dengan kaidah Pancasila\r\n3. Mengadakan beberapa kegiatan lomba yang akan mendorong kualitas hidup ke arah Pancasila', 'tigor_patrick.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `params`
--

CREATE TABLE `params` (
  `num` int(11) NOT NULL,
  `param` varchar(64) NOT NULL,
  `p_value` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `params`
--

INSERT INTO `params` (`num`, `param`, `p_value`) VALUES
(1, 'allow_user_access_realcount', 'true'),
(2, 'allow_access', 'true');

-- --------------------------------------------------------

--
-- Table structure for table `user_pilkasis`
--

CREATE TABLE `user_pilkasis` (
  `num_id` int(11) NOT NULL,
  `nama` varchar(64) CHARACTER SET utf8 NOT NULL,
  `kelas` varchar(24) CHARACTER SET utf8 NOT NULL,
  `username` varchar(32) CHARACTER SET utf8 NOT NULL,
  `user_password` varchar(32) CHARACTER SET utf8 NOT NULL,
  `avatar` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT 'default',
  `pilih_ketua_id` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_pilkasis`
--

INSERT INTO `user_pilkasis` (`num_id`, `nama`, `kelas`, `username`, `user_password`, `avatar`, `pilih_ketua_id`) VALUES
(1, 'Alexzander Purwoko Widiantoro', 'Jepurun', 'Alexz', 'egregh', 'alexz.jpg', 2),
(2, 'Aji Dewa Nata', 'Jepurun', 'Aji', 'hyjrfgrt', 'default', 1),
(3, 'Florensius Dias Prasetyo', 'Jepurun', 'Dias', 'rhjtjykr', 'default', 2),
(4, 'Andreas Adhi Untoro', 'Jepurun', 'Adhi', 'dfheryth', 'default', 1),
(5, 'Agustinus Aldi Ivan Hidayat', 'Jepurun', 'Aldi', 'zsdgre', 'default', 2),
(6, 'Iqbal Rizki Mardani', 'Jepurun', 'Iqbal', 'zesw5r', 'default', 1),
(7, 'Maria Siska', 'Jepurun', 'Siska', '4et4yf', 'default', 0),
(8, 'Katarina Nanda Kinasih', 'Jepurun', 'Nanda', 'dfy5gbvg', 'default', 1),
(9, 'Helena Tessa', 'Jepurun', 'Helen', 'thyu6u5', 'default', 0),
(10, 'Veronika Dewi Mulia Putri', 'Jepurun', 'Dewi', 'bghtbh', 'default', 2),
(11, 'Yosefa Zefanya Anjani', 'Jepurun', 'Sefa', 'rer4sdf', 'default', 1),
(12, 'Yulius Tri Yupendra', 'Jepurun', 'Yupen', '454fsde', 'default', 1),
(13, 'Veronika Widi Saputri', 'Pucanganom', 'Vero', '4fdfw', 'default', 0),
(14, 'Stephani Dyah Astuningtyas', 'Jepurun', 'Tyas', 'f4fssef', 'default', 0),
(15, 'Ardhiyan Chendel', 'Jepurun', 'Diyan', '4r4y5f', 'default', 0),
(16, 'Brian Dista Setya Budhi', 'XI TKJ 1', 'Brian', 'vfre4t342', 'default', 0),
(17, 'Zenny Andhi Prilliany', 'Jakarta', 'Zenny', '56ygerw', 'default', 0),
(18, 'Abid Eka Nurrahman', 'XI TKJ 1', 'Abid', '4t44tf', 'default', 2),
(19, 'Fandy Hidayat', 'Pucanganom', 'Fandy', '5t5y6yr', 'default', 0),
(20, 'Anang Vrisqi Ramadhan', 'XI TKJ 1', 'Anang', '09459gvr', 'default', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `allowed_users`
--
ALTER TABLE `allowed_users`
  ADD PRIMARY KEY (`num_id`);

--
-- Indexes for table `kandidat`
--
ALTER TABLE `kandidat`
  ADD PRIMARY KEY (`num_id`);

--
-- Indexes for table `params`
--
ALTER TABLE `params`
  ADD PRIMARY KEY (`num`);

--
-- Indexes for table `user_pilkasis`
--
ALTER TABLE `user_pilkasis`
  ADD PRIMARY KEY (`num_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `allowed_users`
--
ALTER TABLE `allowed_users`
  MODIFY `num_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `kandidat`
--
ALTER TABLE `kandidat`
  MODIFY `num_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `params`
--
ALTER TABLE `params`
  MODIFY `num` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `user_pilkasis`
--
ALTER TABLE `user_pilkasis`
  MODIFY `num_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
