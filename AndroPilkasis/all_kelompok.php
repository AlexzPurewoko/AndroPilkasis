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
      $group_sql = "SELECT kelas FROM user_pilkasis GROUP BY kelas ORDER BY count(*)";
      $query_group = mysqli_query($con, $group_sql);
      $result["isAuthSuccess"] = "true";

      $returned_arr = array();
      while($row = mysqli_fetch_array($query_group)){
        array_push($returned_arr, $row['kelas']);
      }
      $result["group_list"] = $returned_arr;
    } else {
      $result["isAuthSuccess"] = "false";
    }

    echo json_encode(array('groups' => $result));
    mysqli_close($con);
  }
 ?>
