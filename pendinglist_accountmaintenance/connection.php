<?php
$host ="localhost:3306";
$user = "root";
$pass = "";
$dbnm= "jwdb";
$con = mysql_connect($host,$user,$pass,$dbnm) or die (mysql_error());
$db  = mysql_select_db("jwdb",$con);
if (!$con || !$db) die("Koneksi Gagal: " . mysql_error());  
else //echo "Koneksi Database ".$dbnm." Berhasil ...<br/><br/>";
$judul = "Mega Data Integration";
?>