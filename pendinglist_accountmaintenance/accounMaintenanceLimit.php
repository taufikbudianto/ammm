<?php 
include 'pagination1.php';
include 'connection.php';
$condition= $_GET['numb'];
$hist=$_GET['hist'];
$sql =  "select a.id,a.status_routing, a.dateCreated,a.c_txtNoTicket, a.c_fldNamaReq, a.c_fldNipRequester,
	a.c_fldApprAnalyst,a.c_fldStatusAdmin,a.c_fldNamaNasabah,(case when a.c_fldRekomen='true' then concat('Need Rekomendasi ',a.c_fldRmuConfirm)
	when a.c_fldRekomen='false' then (case when a.c_fldApprAnalyst='approved' then concat('Approved By ',a.c_fldApprAnalystBy)
	when a.c_fldApprAnalyst='rejected' then concat('Rejected By ',a.c_fldApprAnalystBy) 
	when a.c_fldApprAnalyst is null then concat('Waiting Response Analyst')end)
	when a.c_fldRekomen is null then concat('Waiting Response Analyst') end)Analyst,
	(case when a.c_fldRmuConfirm is not null then (case when a.c_doneRekomendasiRMU is not null then concat('Rekomendasi Done')
	when (a.c_doneRekomendasiRMU is null and a.c_fldRmuConfirm='rmu') then concat('waiting response rmu')
	when (a.c_doneRekomendasiRMU is null and a.c_fldRmuConfirm <>'rmu') then concat('********') end)
	 when a.c_fldRmuConfirm is null then concat('*******') end)rmu ,
	 (case when a.c_fldRmuConfirm is not null then (case when a.c_doneKonfirmasi is not null then concat('Rekomendasi Done')
	when (a.c_doneKonfirmasi is null and a.c_fldRmuConfirm='confirm') then concat('waiting response Konfirmasi')
	when (a.c_doneKonfirmasi is null and a.c_fldRmuConfirm <>'confirm') then concat('********') end)
	 when a.c_fldRmuConfirm is null then concat('*******') end)konfirmasi ,(case when a.c_fldRkomendasiDM='true' then concat('Need Rekomendasi DM')
	when a.c_fldRkomendasiDM='false' then (case when a.c_fldStatApprHeadAnalyst='approved' then concat('Approved By ',a.c_fldApprHeadAnalystBy)
	when a.c_fldStatApprHeadAnalyst='rejected' then concat('Rejected By ',a.c_fldApprHeadAnalystBy) 
	when a.c_fldStatApprHeadAnalyst is null then concat('Waiting Response Head Analyst')end)
	when a.c_fldRkomendasiDM is null then concat('Waiting Response Head Analyst') end)HeadAnalyst,
	 (case when a.c_fldRkomendasiDM='true' then (case when a.c_fldDoneRecDM is null then concat('Waiting Response DM')
	  when a.c_fldDoneRecDM is not null then concat('Done By DM') end)
		when a.c_fldRkomendasiDM <>'true' then (case when a.c_fldDoneRecDM is not null then concat('Done By DM')
			when a.c_fldDoneRecDM is null then concat('*******') end) end)DM,(case when a.c_fldStatusAdmin is not null then concat('Done')
				when a.c_fldStatusAdmin is null then concat('Waiting.....') end)admin 
	from app_fd_accountmaint a where (a.status_routing='Case AM' or a.c_flagWFcontrol='am') ";
	
if(isset($_REQUEST['keyword']) && $_REQUEST['srchby']<>""){
	$keyword=$_REQUEST['keyword'];
	$dt = $_REQUEST['srchby'];
	if($hist=="ind"){
		$reload = "accounMaintenanceLimit.php?pagination=true&hist=ind&keyword=$keyword&srchby=$dt&numb=$condition";
	}else{
		$reload = "accounMaintenanceLimit.php?pagination=true&keyword=$keyword&srchby=$dt&hist=all";
		$sql = $sql." and ".$_REQUEST['srchby']." like '%".$_REQUEST['keyword']."%' order by id desc limit 100";
	}  
	
	$result = mysql_query($sql);
	$rwdt = mysql_fetch_array($result);
	$rowcount = mysql_num_rows($result);

}else{
	if($hist=="ind"){
		$reload =  "accounMaintenanceLimit.php?pagination=true&hist=ind&numb=$condition";
		$sql = $sql." where a.c_fldNipRequester = '$condition' or a.c_fldNipRMU = '$condition' or a.c_fldNipConfirm = '$condition' or a.c_fldNipAnalyst = '$condition' or a.c_fldNipHeadAnalyst = '$condition'";	
	}else{
		$reload = "accounMaintenanceLimit.php?pagination=true&hist=all";
		$sql = $sql." limit 100";
	}   
	
	$result = mysql_query($sql);
	$rwdt = mysql_fetch_array($result);
	$rowcount = mysql_num_rows($result);
}

$rpp = 10; // jumlah record per halaman
$page = intval($_GET["page"]);
if($page<=0) $page = 1;
$tcount = mysql_num_rows($result);
$tpages = ($tcount) ? ceil($tcount/$rpp) : 1; // total pages, last page number
$count = 0;
$i = ($page-1)*$rpp;
$no_urut = ($page-1)*$rpp;

error_reporting(0)
?>
<!doctype html>
<html>
    <head>
        <title>History Request Data</title>
        <link rel="stylesheet" href="css/bootstrap.min.css"/>
		<style>
		table {
			border-collapse: collapse;
			width: 100%;
		}

		th, td {
			text-align: left;
			padding: 8px;
		}

		tr:nth-child(even){background-color: #f2f2f2}

		th {
			background-color: #4CAF50;
			color: white;
		}
		</style>
        <script>
			function choose(){
				var srch = document.getElementById("srchby").value;
				var equal = document.getElementById("keyword");
				//alert(equal);
				var btns = document.getElementById("btn");
				//alert(srch);
				if (srch!='')
				{
					//alert("Masuk");
					equal.style.display="block";
					if (srch == "dateCreated"){
						equal.placeholder = "Format Tanggal yyyy-mm-dd (2016-03-14)";
					}else{
						equal.placeholder = "Isikan "+srch;
					}
					//equal.value = "Isikan "+srch;
					btns.style.display="block";
				}
				else
				{
					alert("kgk");
					equal.style.display="none";
					//equal.value = '';
					btns.style.display="none";
				}
			}
		</script>
    </head>
    <body>
	<nav class="navbar navbar-inverse" role="navigation">
     <div class="container-fluid">
<!--header section -->
          <div class="navbar-header">
               <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
               <span class="sr-only">Toggle navigation</span>
               <span class="icon-bar"></span>
               <span class="icon-bar"></span>
               <span class="icon-bar"></span>
               </button>
               <a class="navbar-brand" href="#"><Strong>Account Maintenance</Strong></a>
          </div>
<!-- menu section -->
          <div class="collapse navbar-collapse navbar-ex1-collapse">
     
			    <ul class="nav navbar-nav navbar-right">
               <li><a href="#">Back</a></li>
               </ul>
			   <ul class="nav navbar-nav navbar-left">
               <li class="active"><a href="accounMaintenanceLimit.php?hist=ind&numb=<?php echo $condition;?>">History ALL</a></li>
			   <li><a href="export.php">Report Data</a></li>
               </ul>
          </div>
     </div>
</nav>
		<p><h1><center><strong>History All E-Form Request Account Maintenance system</strong></center></h1></p>
        <div class="container" style="margin-left: 50px">
            <div class="row">
                <div class="col-lg-7">
                    <!--muncul jika ada pencarian (tombol reset pencarian)-->
                    <?php
                    if($_REQUEST['keyword']<>""){
                    ?>
                        <a class="btn btn-default btn-outline" href="accounMaintenanceLimit.php?hist=ind&numb=<?php echo $condition;?>"> Reset Pencarian</a>
                    <?php
                    }
                    ?>
                </div>
                <div class="col-lg-5">
                    <form method="post" action="accounMaintenanceLimit.php?hist=ind&numb=<?php echo $condition;?>">
                        <div class="form-group input-group">
							<table>
								<tr>
									<td>
										<select name="srchby" id="srchby" class="form-control" onChange="choose();">
										<option value="">Select</option>
										<option value="c_fldNamaReq">Nama Requester</option>
										<option value="dateCreated">Created Date</option>
										<option value="c_fldNamaNasabah">Nama Nasabah</option>
										<option value="c_txtNoTicket">Nomer Tiket</option>
										</select>
									</td>
									<td>
										<input type="text" name="keyword" id="keyword" class="form-control" placeholder="Search Date Call (<?php echo date('Y-m-d');?>)" value="<?php echo $_REQUEST['keyword']; ?>">
									</td>
									<td>
										<span class="input-group-btn">
											<button class="btn btn-primary" type="submit"><span class="glyphicon glyphicon-search"></span>
											</button>
										</span>
									</td>
								</tr>
							</table>
                        </div>
                    </form>
                </div>
            </div>
           <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>#</th>
                        <th align="center"><center>Tanggal Permohonan</center></th>
                        <th align="center"><center>Nama Requester</center></th>
						<th align="center"><center>NIP Requester</center></th>
						 <th align="center"><center>Nomer Tiket</center></th>
                        <th align="center"><center>Nama Nasabah</center></th>
                        <th align="center"><center>Analyst</center></th>
						<th align="center"><center>RMU</center></th>
						<th align="center"><center>Konfirmasi</center></th>
                        <th align="center"><center>Head Analyst</center></th>
                        <th align="center"><center>DM</center></th>
						<th align="center"><center>Status Admin</center></th>
						<th align="center"><center>Action</center></th>
                    </tr>
                </thead>
                <tbody>
                    <?php
                    while(($count<$rpp) && ($i<$tcount)) {
                        mysql_data_seek($result,$i);
                        $data = mysql_fetch_array($result);
						$createdate = $data['dateCreated'];
						$reqname = $data['c_fldNamaReq'];
						$reqnip = $data['c_fldNipRequester'];
						$tiket = $data['c_txtNoTicket'];
						$nasabah = $data['c_fldNamaNasabah'];
						$apprAnalis = $data['Analyst'];
						$rmu = $data['rmu'];
						$konfirmasi = $data['konfirmasi'];
						$headAnalis = $data['HeadAnalyst'];
						$DM = $data['DM'];
						$Admin = $data['admin'];
						$ids=$data['id'];
						
                    ?>
                    <tr>
                        <td><?php echo ++$no_urut;?></td>
                        <td><?php echo $createdate; ?></td>
						<td><?php echo $reqname; ?></td>
                       	<td><?php echo $reqnip; ?></td>
						<td><?php echo $tiket; ?></td>
						<td><?php echo $nasabah; ?></td>
						<td><?php echo $apprAnalis; ?></td>
						<td><?php echo $rmu; ?></td>
						<td><?php echo $konfirmasi; ?></td>
						<td><?php echo $headAnalis; ?></td>
                        <td><?php echo $DM; ?></td>
						<td><?php echo $Admin; ?></td>
						<td><a href="javascript: void(0)" onClick="popupwin('http://10.11.88.234:8185/jw/web/userview/acctMaintenanceSystem/userviewAcctMaint//C0D3638AE0DD48BDA84BF1B525A60B21?_mode=edit&id=<?php echo $data['id'];?>'); return false;" class = "btn btn-primary">Detail</a></td>
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
        <script type="text/javascript">
            function cls(a){
				window.opener.location = a;
				window.close();
            }
			function hkp(b){
							window.opener.location = b;
							window.close();
						}
						function fail(){
							alert("Link Hanya Untuk Status Data HOUSEKEEPING");
						}
        </script>
		<script type="text/javascript">
		function popupwin(url) {
		var width  = 970;
		var height = 480;
		var left   = (screen.width  - width)/2;
		var top    = (screen.height - height)/2;
		var params = 'width='+width+', height='+height;
		params += ', top='+top+', left='+left;
		params += ', scrollbars=1';
		newwin=window.open(url,'popupwin', params);
		if (window.focus) {newwin.focus()}
		return false;
}
</script>
    </body>
</html>
