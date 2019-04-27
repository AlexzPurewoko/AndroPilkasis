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
  $group_id = $_POST['group_id'];
  $result = array();
  if($auth_condition){
    $result["isAuthSuccess"] = "true";
    $allow_user_sql = "SELECT COUNT(1) AS allow FROM allowed_users WHERE group_id='$group_id' AND username='$user_name'";
    $query_allow_user = mysqli_query($con, $allow_user_sql);
    $allow_user_assoc = mysqli_fetch_assoc($query_allow_user);
    if($allow_user_assoc['allow'] == '0')
      $result['hasAllowPermission'] = 'false';
    else
      $result['hasAllowPermission'] = 'true';


    // check if the user has submit their selected leader
    $has_selected_sql = "SELECT pilih_ketua_id FROM user_pilkasis WHERE username LIKE '$user_name' AND user_password LIKE '$passwd'";
    $query_has_selected = mysqli_query($con, $has_selected_sql);
    $has_selected_assoc = mysqli_fetch_assoc($query_has_selected);
    if($has_selected_assoc['pilih_ketua_id'] == '0')
      $result['hasSelectedLeader'] = 'false';
    else
      $result['hasSelectedLeader'] = 'true';
  } else {
    $result["isAuthSuccess"] = "false";
  }

  echo json_encode(array('allow_access' => $result));
  mysqli_close($con);
}
 ?>
