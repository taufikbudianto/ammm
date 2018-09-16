<?php
//$host = "10.14.18.223:3318";
//$user = "root";
//$pass = "p@ssw0rdM3ga";
$host = "10.11.88.234:3315";
$user = "root";
$pass="";
$dbnm= "jwdb";
$con = mysql_connect($host,$user,$pass,$dbnm) or die (mysql_error());
$db  = mysql_select_db("jwdb",$con);
if (!$con || !$db) die("Koneksi Gagal: " . mysql_error());  
else //echo "Koneksi Database ".$dbnm." Berhasil ...<br/><br/>";
$judul = "Mega Data Integration";
?>