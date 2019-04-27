/**
 * Copyright @2019 by Alexzander Purwoko Widiantoro <purwoko908@gmail.com>
 * @author APWDevs
 *
 * Licensed under GNU GPLv3
 *
 * This module is provided by "AS IS" and if you want to take
 * a copy or modifying this module, you must include this @author
 * Thanks! Happy Coding!
 *
 *
 * This file is for handling DB Connection, so you must change this default
 * configuration before using, especially USER and PASS. Otherwise (like PilkasisDB) you 
 * must not change default configuration. And Optional is HOST
 */

<?php
  define('HOST', 'localhost');
  define('USER', '[YOUR USER]');
  define('PASS', '[YOUR PASSWORD]');
  define('PilkasisDB', 'AndroPilkasis');

  $con = mysqli_connect(HOST, USER, PASS, PilkasisDB) or die('Cannot connect');

 ?>
