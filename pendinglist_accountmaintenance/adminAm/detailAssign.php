<?php
include 'pagination1.php';
include "connection.php";
 $id=$_GET['id'];
	$sql =  "select a.procid,a.tanggal,a.sebelum,a.sesudah,concat(b.firstname,' ( ',
						a.sebelum,' )')as sebelumm,(select concat(firstName,'(',a.sebelum,')') from dir_user where username COLLATE utf8_general_ci=a.sebelum)as dataa,concat(b.firstname,' (',
						a.sesudah,' )')as sesudahh from tbl_assignacctmaintenance a join dir_user b
								on a.sesudah COLLATE utf8_general_ci=b.username where a.procid='$id' order by tanggal asc";
	$reload = "detailAssign.php?pagination=true&id=".$id;
    $result = mysql_query($sql);
	$rwdt = mysql_fetch_array($result);
	$rowcount = mysql_num_rows($result);    
    $rpp = 3; // jumlah record per halaman
    $page = intval($_GET["page"]);
    if($page<=0) $page = 1;  
    $tcount = mysql_num_rows($result);
    $tpages = ($tcount) ? ceil($tcount/$rpp) : 1; // total pages, last page number
    $count = 0;
    $i = ($page-1)*$rpp;
    $no_urut = ($page-1)*$rpp;
	error_reporting(0) 
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <link   href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery-1.11.1.min.js"></script>
		<script>
</script>
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
    			<h3><center><strong>Detail Assign</strong></center></h3>
    		</div>
				<table class="table table-striped table-bordered">
		              <thead>
		                <tr>
		                  <th>No</th>
						  <th>Date Assign To</th>
		                  <th>Before Assign</th>
		                  <th>After Assign</th>
						  <!--<th>Change Assign To</th>-->
		                </tr>
		              </thead>
					  <tbody>
						<?php
						while(($count<$rpp) && ($i<$tcount)) {
                        mysql_data_seek($result,$i);
                        $data = mysql_fetch_array($result);
						$createdate = $data['tanggal'];
						$name1 = $data['dataa'];
						$name2 = $data['sesudahh'];
						?>
                    <tr>
                        <td><?php echo ++$no_urut;?></td>
                        <td><?php echo $createdate; ?></td>
						<td><?php echo $name1; ?></td>
						<td><?php echo $name2; ?></td>
					</tr>
                    <?php
                        $i++; 
                        $count++;
                    }
                    ?>
				</tbody>
	        </table>
			<div><?php echo paginate_one($reload, $page, $tpages,$rowcount); ?></div>
    	</div>
    </div> <!-- /container -->
  </body>
</html>