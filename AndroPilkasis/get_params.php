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

  $sql_params = "SELECT * FROM params";
  $query_sql  = mysqli_query($con, $sql_params);

  $result = array();
  while($row = mysqli_fetch_array($query_sql)){
    array_push($result, array(
      "num" => $row["num"],
      "param" => $row["param"],
      "value" => $row["p_value"]
    ));
  }
  echo json_encode(array('parameters' => $result));
  mysqli_close($con);
 ?>
