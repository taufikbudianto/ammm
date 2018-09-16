<?php 
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, GET, OPTIONS');
	include 'pagination1.php';
	$name=$_GET['name'];
	$dob=$_GET['dob'];
	//$url ="http://172.29.109.116:8083/dataCompare?callback=?&name=".$name."&dob=".$dob;
	//$url ="http://10.14.18.222:8200/dataCompare/id?callback=?&id=".$name;
	$url ="http://10.14.18.222:8200/dataCompare?callback=?&name=".urlencode($name)."&dob=".$dob;
	$res = get_data($url);
	 $someArray = json_decode($res, true);
	function get_data($url) {
    $ch = curl_init();
    $timeout = 5;
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST,false);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER,false);
    curl_setopt($ch, CURLOPT_MAXREDIRS, 10);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, $timeout);
    $data = curl_exec($ch);
    curl_close($ch);
    return $data;
}
	
?>
<html>
    <head>
        <link rel="stylesheet" href="bootstrap.min.css"/>
		<script src="js/jquery.min.js"></script>
		<script src="js/function.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<style>
		table {
			border-collapse: collapse;
			width: 100%;
		}

		th, td {
			text-align: center;
			vertical-align: middle;
		}

		tr:nth-child(even){background-color: #f2f2f2}

		th {
			background-color: #4CAF50;
			color: white;
		}
		</style>
    </head>
    <body>
        <div class="container">
			<div class="row">
    			<h3><center><strong>Name : <?php echo $name;?> =====&&===== Dob : <?php echo $dob;?></strong></center></h3>
    		</div>
            <table class="table table-bordered">
                <thead>
					<tr>
                        <th>#</th>
                        <th><center>Card Number</center></th>
						<th><center>Card Short Name</center></th>
						<th><center>Card Status</center></th>
						<th><center>Card Date Open</center></th>
						<th><center>Local Name</center></th>
						<th><center>Date Birth</center></th>
						<th><center>Mom Name</center></th>
						<th><center>Credit Limit</center></th>
						<th><center>O/S Bal</center></th>
						<th><center>O/S OPS</center></th>
						<th><center>O/S Auth Amt</center></th>
						<th><center>Stmt Addr H</center></th>
						<th><center>Alamat Rumah</center></th>
						<th><center>Alamat Kantor</center></th>
                    </tr>
                </thead>
                <tbody>
				<?php
				$no =0;
				//if(count($someArray["msg"])!=0){
					foreach ($someArray as $key => $value) {
						$no++;
						echo "<tr><td>".$no."</td>";
						echo "<td>". $value["custnbr"]."</td>";
						echo "<td>". $value["custname"]."</td>";
						echo "<td>". $value["cardstatus"]."</td>";
						echo "<td>". $value["cardopen"]."</td>";
						echo "<td>". $value["cust_LOCAL_NAME"]."</td>";
						echo "<td>". $value["cust_DTE_BIRTH"]."</td>";
						echo "<td>". $value["cust_MOM_NAME"]."</td>";
						echo "<td>". $value["crdacct_CRLIMIT"]."</td>";
						echo "<td>". $value["crdacct_OUTSTD_BAL"]."</td>";
						echo "<td>". $value["crdacct_OUTSTD_INSTL"]."</td>";
						echo "<td>". $value["crdacct_OUTSTD_AUTH_BAL"]."</td>";
						echo "<td>". $value["crdacct_STMT_ADDR_FLG"]."</td>";
						echo "<td>". $value["alamatRmah"]."</td>";
						echo "<td>". $value["alamatKantor"]."</td></tr>";
					}
				//}
					
				?>
                </tbody>
            </table>
        </div>
    </body>
</html>
