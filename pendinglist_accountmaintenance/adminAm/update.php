<?php 	
include "connection.php";

//$tipe = $_POST['tipeAn'];

$user = $_GET['userId'];
$notiket = $_GET['tiket'];
//echo $tipe ;
$id = $_GET['idPro'];

if(!empty($_GET['srchby'])){
$username = $_GET['srchby'];
$insert="insert into tbl_assignacctmaintenance(procid,tanggal,sebelum,sesudah,notiket) values('$id',now(),'$user','$username','$notiket')";
mysql_query($insert);
}else{
$username=$user;
}
$sql ="update app_fd_acctmaintenance set c_fldAssignTo='$username' where id ='$id'";
mysql_query($sql);
header("Location: index.php");
		
?>