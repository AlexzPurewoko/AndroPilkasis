/**
 * Copyright @2019 by Alexzander Purwoko Widiantoro <purwoko908@gmail.com>
 * @author APWDevs
 *
 * Licensed under GNU GPLv3
 *
 * This module is provided by "AS IS" and if you want to take
 * a copy or modifying this module, you must include this @author
 * Thanks! Happy Coding!
 */

<?php
$user_name = $_POST['username'];
$passwd = $_POST['password'];

$sql_oauth = "SELECT count(1) AS cond FROM user_pilkasis WHERE username LIKE '$user_name' AND user_password LIKE '$passwd'";

$result_query_auth = mysqli_query($con, $sql_oauth);
$auth_arr = mysqli_fetch_array($result_query_auth);

$auth_condition = false;
if($auth_arr['cond'] == 1){
  $auth_condition = true;
}
 ?>
