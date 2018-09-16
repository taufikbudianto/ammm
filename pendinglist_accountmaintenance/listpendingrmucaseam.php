<?php 

	include 'pagination1.php';
	include 'connection.php';
	$dates= "";
	$username=$_GET['username'];
	$name=$_GET['name'];
	$error=$_GET['error'];
	$datatest=$_GET['data'];
    if(isset($_REQUEST['keyword']) && $_REQUEST['keyword']<>"" && $_REQUEST['tablename'] <> ""){
//        jika ada kata kunci pencarian (artinya form pencarian disubmit dan tidak kosong)
//        pakai ini
            $keyword=$_REQUEST['keyword'];
			$strTable = $_REQUEST['tablename'];
            $reload = "listpendingrmucaseam.php?pagination=true&keyword=$keyword&username=$username&name=$name&data=$datatest";
			$sql="select * from tbl_acctmaintenancetemp where flagWF='amtormu' and ($strTable like '%$keyword%')";
            $result = mysql_query($sql);
			$rwdt = mysql_fetch_array($result);
			
			$rowcount = mysql_num_rows($result);
		}else{
	//jika tidak ada pencarian pakai ini
            $reload = "listpendingrmucaseam.php?pagination=true&username=$username&name=$name&data=$datatest";
            //$sql =  "select * from tbl_acctmaintenancetemp";
			$sql =  "select * from tbl_acctmaintenancetemp where flagWF='amtormu'";
            $result = mysql_query($sql);
			$rwdt = mysql_fetch_array($result);
			$dates=date('d F Y',strtotime($rwdt['dialdate']));
			$rowcount = mysql_num_rows($result);
        }
        function format_rupiah($rp) {
			$hasil = "Rp." . number_format($rp, 0, "", ".") . ",00";
			return $hasil;
		}
        $rpp = 8; // jumlah record per halaman
        $page = intval($_GET["page"]);
        if($page<=0) $page = 1;  
        $tcount = mysql_num_rows($result);
        $tpages = ($tcount) ? ceil($tcount/$rpp) : 1; // total pages, last page number
        $count = 0;
        $i = ($page-1)*$rpp;
        $no_urut = ($page-1)*$rpp;
        //pagination config end
		error_reporting(0) // tambahkan untuk menghilangkan notice... hehe ?>
<!doctype html>
<html>
    <head>
        <title>Pending List AM</title>
        <link rel="stylesheet" href="bootstrap.min.css"/>
		<script src="js/jquery.min.js"></script>
		<script src="js/function.js"></script>
		<!-- Include all compiled plugins (below), or include individual files as needed -->
		<script src="js/bootstrap.min.js"></script>
		<script>
		  $(document).ready(function(){
					$('#selectAll').click(function() {
						if (this.checked) {
						$(':checkbox').each(function() {
						this.checked = true;                        
					});
				} else {
						$(':checkbox').each(function() {
						this.checked = false;                        
				});
			} 
		});
		   $(document).on("click", ".pass", function () {
		   var tick1 = $(this).data('ticket'); var tick="";
		   if(tick1!=""){ tick=tick1;}else{tick="None";}
		   var crlm1 = $(this).data('cardlimit'); var crlm="";
		   if(crlm1!=""){ crlm=crlm1;}else{crlm="None";}
		   var ctnm1 = $(this).data('custname'); var ctnm="";
		   if(ctnm1!=""){ ctnm=ctnm1;}else{ctnm="None";}
		   var crno1 = $(this).data('cardno'); var crno = "";
		   if(crno1!=""){ crno=crno1;}else{crno="None";}
		   var ctph1 = $(this).data('custph'); var ctph="";
		   if(ctph1!=""){ ctph=ctph1;}else{ctph="None";}
		   var crsts1 = $(this).data('custstatus'); var crsts="";
		   if(crsts1!=""){ crsts=crsts1;}else{crsts="None";}
			 $(".modal-body #tickno").val( tick );
			 $(".modal-title #tickno").val( tick );
			 $(".modal-body #cardlim").val( crlm );
			 $(".modal-body #cardsts").val( crsts );
			 $(".modal-body #custnm").val( ctnm );
			 $(".modal-body #cardno").val( crno );
			 $(".modal-body #custpho").val( ctph );
			 // As pointed out in comments, 
			 // it is superfluous to have to manually call the modal.
			 // $('#addBookDialog').modal('show');
			});
			});
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
                <div class="col-lg-7">
                    <!--muncul jika ada pencarian (tombol reset pencarian)-->
                    <?php
                    if($_REQUEST['keyword']<>""){
                    ?>
                        <a class="btn btn-default btn-outline" href="listpendingrmucaseam.php?username=<?php echo $username; ?>&name=<?php echo $name?>&data=<?php echo $datatest;?>"> Reset Pencarian</a>
                    <?php
                    }
                    ?>
                </div>
                <div class="col-lg-5">
				<!--<h3 align="center"><strong>Pending List Account Maintenance</strong></h3>-->
				<hr/>
                    <form method="get" action="listpendingrmucaseam.php?username=<?php echo $username; ?>&name=<?php echo $name?>&data=<?php echo $datatest;?>">
                        <div class="form-group input-group">
						<table>
							<tr>
                                <td>
									<select name="tablename" class="form-control" value="<?php echo $strTable; ?>">
										<option value=""> -- Filter Field </option>
										<option value="ticketNo">Ticket No</option>
										<option value="cardNum">Card Number</option>
										<option value="subJnsLaporan">Case Type</option>
										<option value="custName">Name Customer</option>
									</select>
								</td>
								<td><input type="text" name="keyword" class="form-control" value="<?php echo $_REQUEST['keyword']; ?>">
								</td>
								<td>
									<span class="input-group-btn">
										<button class="btn btn-primary" type="submit">Cari
										</button>
									</span>
								</td>
							</tr>	
						</table>
                            
                        </div>
                    </form>
                </div>
            </div><!--  <form method="Post" action="test.php" http://10.11.88.234:8185>-->
		 <!-- <form method="post" action="http://10.11.88.234:8185/ServletWeb/servlet">-->
			    <!--<form method="post" action="http://172.29.109.116:8083/complete">-->
				<form method="post" action="http://10.14.18.222:8200/complete">
            <table class="table table-bordered">
                <thead>
					<tr>
                        <th>#</th>
						<!-- <th><input type="checkbox" id="selectAll" value="selectAll"> Select All</th>-->
						<th><input type="checkbox" id="selectAll" value="selectAll"></th>
                        <th><center>Ticket Number</center></th>
						<th><center>Card Number</center></th>
                        <!--<th><center>Card Limit</center></th>
						<th><center>Card Status</center></th>-->
						<th><center>Case Type</center></th>
						<th><center>Cust Name</center></th>
					<!--	<th><center>Action</center></th>-->
                    </tr>
                </thead>
                <tbody>
                    <?php
					$val='http://10.11.88.234:8185/jw/web/userview/AcctMaintenanceSystem/userviewAcctMaint//795B662514694DA695AACAF9792EF0A3';
                    while(($count<$rpp) && ($i<$tcount)) {
                        mysql_data_seek($result,$i);
                        $data = mysql_fetch_array($result);
						$acct = substr($data['account'],13);
						$ticketid = $data ['ticketNo'];
						$cardlimit = $data ['cardLimit'];
						$custname = $data ['custName'];
						$cardno = $data ['cardNum'];
						$caseType = $data ['subJnsLaporan'];
						$custph = $data ['custPhone'];
						$custstatus = $data ['cardStatus'];
						$datamodal="data-ticket=$ticketid data-cardlimit=$cardlimit data-custname=$custname data-cardno=$cardno data-custph=$custph data-custstatus=$custstatus";
                    ?>
                    <tr>
                        <td><?php echo ++$no_urut;?></td>
						<td align="center" bgcolor="#FFFFFF"><input name="checkbox2" type="checkbox" value="<?php echo $data ['ticketNo']; ?>"></td>
                        <td><a href="#myModal" data-toggle="modal" <?php echo $datamodal;?> class="pass"><?php echo $data ['ticketNo']; ?></a></td>
						<td><?php echo $data ['cardNum']; ?></td>
						<!--<td><?php echo $data ['cardLimit']; ?></td>
						<td><?php echo $data ['cardStatus']; ?></td>-->
						<td><?php echo $data ['subJnsLaporan']; ?></td>
						<td ><?php echo $data ['custName']; ?></td>					
						
						<!--<td><a target="_parent" href='http://10.11.88.234:8185/jw/web/userview/acctMaintenanceSystem/userviewAcctMaint//795B662514694DA695AACAF9792EF0A3?histFlag=%22pendingList"&data=<?php echo $data ['ticketNo'];?>&data2=<?php echo $data ['cardNum'];?>' class='btn btn-primary' />Follow Up</td>-->
                    </tr>
                    <?php
                        $i++; 
                        $count++;
                    }
                    ?>
					<tr>
							<td colspan="6"> </td>
					</tr>
					<tr>
						<td colspan="3">  <input type="hidden" name="username" value="<?php echo $username;?>">
						<input type="hidden" name="name" value="<?php echo $name;?>">
						<input type="hidden" name="routedata" value="toAM">
						<input type="hidden" name="route" value="rmu"></td>
						<?php
						
							$querys = "select a.userId,b.firstName from dir_user_group a  join dir_user b on a.userId =b.username where a.groupId='grpRMULimitAM'";
							$result2 = mysql_query($querys) or die(mysql_error()."[".$querys."]");
						?>
						<td colspan ="2"><select name="assignTo" id="assignTo" class="form-control" >
							<option value="">Select</option>
							<?php 
								while ($row = mysql_fetch_array($result2))
								{
								echo "<option value=".$row['userId'].">".$row['firstName']."(".$row['userId'].")</option>";
								}
							?>   
						</select></td>
						<td><input  name="oke" id="oke" type="submit" value="Follow Up" class='btn btn-primary' ></td>
						<!--<td><a target="_parent" href='http://10.11.88.234/AccountMaintenance1/test.php' class='btn btn-primary' />Follow Up</td>-->
					</tr>
                </tbody>
            </table>
			</form>
            <div><?php echo paginate_one($reload, $page, $tpages,$rowcount); ?></div>
			<!-- Detail Modal -->
            <div id="myModal" class="modal fade" role="dialog">
              <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Detail Pending List : <input type="text" name="tickno" id="tickno" value="" style="border: none;background: transparent;"/></h4>
                  </div>
                  <div class="modal-body">
                  <table width="100%" align="center">
                  	<tr>
                     <td width="350">Ticket Number</td><td>:</td><td><input type="text" name="tickno" id="tickno" value="" style="border: none;background: transparent; float:right;"/></td>
                     <td width="350">Card Limit</td><td>:</td><td><input type="text" name="cardlim" id="cardlim" value="" style="border: none;background: transparent;"/></td> 
                     </tr>
                     <tr>
                     <td>Card Status</td><td>:</td><td><input type="text" name="cardsts" id="cardsts" value="" style="border: none;background: transparent;"/></td>
                     <td>Customer Name</td><td>:</td><td><strong><input type="text" name="custnm" id="custnm" value="" style="border: none;background: transparent;"/></strong></td>
                     </tr>
                     <tr>
                     <td>Card Number</td><td>:</td><td><input type="text" name="cardno" id="cardno" value="" style="border: none;background: transparent;"/></td>
                     <td>Customer Phone</td><td>:</td><td><input type="text" name="custpho" id="custpho" value="" style="border: none;background: transparent;"/></td>  
                     </tr>
                  </table>

                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                  </div>
                </div>
            
              </div>
            </div>
        </div>
		 <script type="text/javascript">
            function cls(a){
				window.opener.location = a;
				window.close();
            }
        </script>
    </body>
</html>