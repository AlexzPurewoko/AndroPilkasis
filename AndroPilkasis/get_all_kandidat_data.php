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
      $kandidat_sql = "SELECT * FROM kandidat ORDER BY num_id";
      $query_kandidat = mysqli_query($con, $kandidat_sql);
      $result["isAuthSuccess"] = "true";
      while($row = mysqli_fetch_array($query_kandidat)){
        $arr = array(
          'num_id' => $row['num_id'],
          'nama' => $row['nama'],
          'kelas' => $row['kelas'],
          'visi' => $row['visi'],
          'misi' => $row['misi'],
          'avatar' => $row['avatar']
        );
        array_push($result, $arr);
      }
    } else {
      $result["isAuthSuccess"] = "false";
    }

    echo json_encode(array('kandidat' => $result));
    mysqli_close($con);
  }
 ?>
