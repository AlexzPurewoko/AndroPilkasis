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
  require_once('auth_session.php');
  $result = array();
  if($auth_condition){
    $select_user_sql = "SELECT * FROM user_pilkasis WHERE username LIKE '$user_name' AND user_password LIKE '$passwd' LIMIT 1";
    $query_select_datasheet = mysqli_query($con, $select_user_sql);
    $result["isAuthSuccess"] = "true";
    $user_arr = mysqli_fetch_array($query_select_datasheet);

    array_push($result, array(
      "num_id"  => $user_arr["num_id"],
      "nama"    => $user_arr["nama"],
      "kelas"    => $user_arr["kelas"],
      "username"    => $user_arr["username"],
      "avatar"  => $user_arr["avatar"],
      "pilih_ketua_id" => $user_arr["pilih_ketua_id"]
    ));
  } else {
    $result["isAuthSuccess"] = "false";
  }
  echo json_encode(array('userDatasheet' => $result));
  mysqli_close($con);
  }
 ?>
