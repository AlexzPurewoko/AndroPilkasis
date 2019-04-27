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
  require_once('db_conn.php');

  if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $user_name = $_POST['username'];
    $passwd = $_POST['password'];
    $result = array();

    // check if the user is registered, but no password checking
    $sql_oauth = "SELECT count(1) AS cond FROM user_pilkasis WHERE username LIKE '$user_name'";
    $result_query_auth = mysqli_query($con, $sql_oauth);
    $auth_arr = mysqli_fetch_array($result_query_auth);

    if($auth_arr['cond'] == 1){
      $result["isUserRegistered"] = "true";
      $sql_passwd_check = "SELECT count(1) AS pwd FROM user_pilkasis WHERE username LIKE '$user_name' AND user_password LIKE '$passwd'";
      $result_passwd_check = mysqli_query($con, $sql_passwd_check);
      $pwd_arr = mysqli_fetch_array($result_passwd_check);
      if($pwd_arr['pwd'] == 0)
        $result['passwordVerification'] = 'false';
      else
        $result['passwordVerification'] = 'true';

    } else {
      $result["isUserRegistered"] = "false";
    }
    echo json_encode(array('authResponse' => $result));
    mysqli_close($con);
  }
 ?>
