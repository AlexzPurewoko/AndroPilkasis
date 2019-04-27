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
    $result["isAuthSuccess"] = "true";

    // count total user available
    $count_user_sql = "SELECT COUNT(1) AS count_user FROM user_pilkasis";
    $count_user_query = mysqli_query($con, $count_user_sql);
    $fetched_array = mysqli_fetch_array($count_user_query);
    $result["count_user_total"] = $fetched_array["count_user"];
    // count total user that not selected the leader
    $count_golput_sql = "SELECT COUNT(1) AS count_golput FROM user_pilkasis WHERE pilih_ketua_id LIKE '0'";
    $count_golput_query = mysqli_query($con, $count_golput_sql);
    $fetched_array = mysqli_fetch_array($count_golput_query);
    $result["count_golput"] = $fetched_array["count_golput"];

    // count based on kandidat
    $kandidat_sql = "SELECT * FROM kandidat ORDER BY num_id";
    $kandidat_query = mysqli_query($con, $kandidat_sql);

    while($row_kandidat = mysqli_fetch_array($kandidat_query)){
      $kandidat_id = $row_kandidat["num_id"];
      $kandidat_nama = $row_kandidat["nama"];
      $kandidat_group = $row_kandidat["kelas"];
      $kandidat_visi = $row_kandidat["visi"];
      $kandidat_misi = $row_kandidat["misi"];
      $kandidat_avatar = $row_kandidat["avatar"];

      $count_selected_sql = "SELECT count(1) AS count_selected FROM user_pilkasis WHERE pilih_ketua_id LIKE '$kandidat_id'";
      $count_selected_query = mysqli_query($con, $count_selected_sql);
      $count_selected = mysqli_fetch_assoc($count_selected_query);
      array_push($result, array(
        "num_id"          => $kandidat_id,
        "nama"            => $kandidat_nama,
        "group"           => $kandidat_group,
        "visi"            => $kandidat_visi,
        "misi"            => $kandidat_misi,
        "avatar"          => $kandidat_avatar,
        "count_selected"  => $count_selected['count_selected']
      ));
    }
  } else {
    $result["isAuthSuccess"] = "false";
  }
  echo json_encode(array('realcount' => $result));
}
  mysqli_close($con);
 ?>
