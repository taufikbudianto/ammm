<?php

include 'pagination1.php';
include 'connection.php';

$dates = "";
$jumlah = $_GET['jumlah'];
$username = $_GET['username'];
$name = $_GET['name'];
$error = $_GET['error'];
$datatest = $_GET['data'];
$route=$_GET['route'];
$roting="";
$group="";
if($route=="am"){
    $group="groupAnalystLimitAM";
    $roting="KK-Account Maintenance";
}elseif ($route=="dm"){
    $group="groupDM";
    $roting="KK-Data Maintenance";
}elseif ($route=="rmu"){
    $roting="KK-Fraud Prevention";
    $group="grpRMULimitAM";
}
if ($_GET['error'] <> "") {
    if ($error == "sukses") {
        echo '<script language="javascript">';
        echo 'alert("Generate Id Joget Sukses")';
        echo '</script>';
    } elseif ($error == "gagal") {
        echo '<script language="javascript">';
        echo 'alert("Please Check Again")';
        echo '</script>';
    } elseif ($error == "duplicate") {
        echo '<script language="javascript">';
        echo 'alert("Ticket Number Cannot Duplicate")';
        echo '</script>';
    } elseif ($error == "tiketnull") {
        echo '<script language="javascript">';
        echo 'alert("Please Select Your Ticket")';
        echo '</script>';
    } elseif ($error == "assigntonull") {
        echo '<script language="javascript">';
        echo 'alert("Please Select Assign to User (Assign To user Cannot Null)")';
        echo '</script>';
    }
}
if (isset($_REQUEST['keyword']) && $_REQUEST['keyword'] <> "" && $_REQUEST['tablename'] <> "") {
    $keyword = $_REQUEST['keyword'];
    $strTable = $_REQUEST['tablename'];
} else {
    $keyword = 1;
    $strTable = 1;
        }
$reload = "listpendingam.php?pagination=true&username=$username&name=$name&data=$datatest&jumlah=$jumlah&route=$route";
$sql = "select * from tbl_acctmaintenancetemp 
					where assignedTo='$roting' and flagCCBM in ('Open','In Progress') and ($strTable like '%$keyword%')";
$result = mysql_query($sql);
$rwdt = mysql_fetch_array($result);
$dates = date('d F Y', strtotime($rwdt['dialdate']));
$rowcount = mysql_num_rows($result);
$rpp = $jumlah; // jumlah record per halaman
$page = intval($_GET["page"]);
if ($page <= 0) $page = 1;
$tcount = mysql_num_rows($result);
$tpages = ($tcount) ? ceil($tcount / $rpp) : 1; // total pages, last page number
$count = 0;
$i = ($page - 1) * $rpp;
$no_urut = ($page - 1) * $rpp;
//pagination config end
error_reporting(0) // tambahkan untuk menghilangkan notice... hehe ?>
<html>
<head>
    <title>Pending List AM</title>
    <link rel="stylesheet" href="bootstrap.min.css"/>
    <script src="js/jquery.min.js"></script>
    <script src="js/function.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->

    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript">
        function change(val) {
            //window.document.domain = "10.14.18.222:8081";
            //parent.panjang(val);
            alert(val);
            window.location = "?username=<?php echo $username;?>&name=<?php echo $name;?>&data=toAM&jumlah=" + val;
        }

        $(document).ready(function () {

            $('#selectAll').click(function () {
                if (this.checked) {
                    $(':checkbox').each(function () {
                        this.checked = true;
                    });
                } else {
                    $(':checkbox').each(function () {
                        this.checked = false;
                    });
                }
            });
            $(document).on("click", ".pass", function () {
                var tick1 = $(this).data('ticket');
                var tick = "";
                if (tick1 != "") {
                    tick = tick1;
                } else {
                    tick = "None";
                }
                var crlm1 = $(this).data('cardlimit');
                var crlm = "";
                if (crlm1 != "") {
                    crlm = crlm1;
                } else {
                    crlm = "None";
                }
                var ctnm1 = $(this).data('custname');
                var ctnm = "";
                if (ctnm1 != "") {
                    ctnm = ctnm1;
                } else {
                    ctnm = "None";
                }
                var crno1 = $(this).data('cardno');
                var crno = "";
                if (crno1 != "") {
                    crno = crno1;
                } else {
                    crno = "None";
                }
                var ctph1 = $(this).data('custph');
                var ctph = "";
                if (ctph1 != "") {
                    ctph = ctph1;
                } else {
                    ctph = "None";
                }
                var crsts1 = $(this).data('custstatus');
                var crsts = "";
                if (crsts1 != "") {
                    crsts = crsts1;
                } else {
                    crsts = "None";
                }
                var memoccbm1 = $(this).data('memoccbm');
                var memoccbm = "";
                if (memoccbm1 != "") {
                    memoccbm = decodeURI(memoccbm1);
                    memoccbm=unescape(memoccbm);
                } else {
                    memoccbm = "None";
                }
                $(".modal-body #tickno").val(tick);
                $(".modal-title #tickno").val(tick);
                $(".modal-body #cardlim").val(crlm);
                $(".modal-body #cardsts").val(crsts);
                $(".modal-body #custnm").val(ctnm);
                $(".modal-body #cardno").val(crno);
                $(".modal-body #custpho").val(ctph);
                $(".modal-body #description").val(memoccbm);
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

        tr:nth-child(even) {
            background-color: #f2f2f2
        }

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
            if ($_REQUEST['keyword'] <> "") {
                ?>
                <a class="btn btn-default btn-outline"
                   href="listpendingam.php?username=<?php echo $username; ?>&name=<?php echo $name; ?>&data=<?php echo $datatest; ?>&jumlah=10&route=<?php echo $route; ?>">
                    Reset Pencarian</a>
                <?php
            }
            ?>
        </div>
        <div class="col-lg-5">
            <!--<h3 align="center"><strong>Pending List Account Maintenance</strong></h3>-->
            <hr/>
            <form method="get"
                  action="listpendingam.php?username=<?php echo $username; ?>&name=<?php echo $name ?>&data=<?php echo $datatest; ?>&jumlah=<?php echo $jumlah; ?>&route=<?php echo $route; ?>">
                <div class="form-group input-group">
                    <table>
                        <tr>
                            <td>
                                <select name="jumlah" id="jumlah" class="form-control" onChange="change(this.value);">
                                    <option value="<?php echo $jumlah; ?>"><?php echo $jumlah; ?></option>
                                    <option value="10">10</option>
                                    <option value="20">20</option>
                                    <option value="35">35</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select>
                            </td>
                            <td>
                                <select name="tablename" class="form-control" value="<?php echo $strTable; ?>">
                                    <option value=""> -- Filter Field</option>
                                    <option value="ticketNo">Ticket No</option>
                                    <option value="cardNum">Card Number</option>
                                    <option value="subJnsLaporan">Case Type</option>
                                    <option value="custName">Name Customer</option>
                                </select>
                            </td>
                            <td>
                                <input type="text" name="keyword" class="form-control"
                                       value="<?php echo $_REQUEST['keyword']; ?>">
                            </td>
                            <input type="hidden" name="name" value="<?php echo $name; ?>">
                            <input type="hidden" name="routedata" value="<?php echo $datatest; ?>">
                            <input type="hidden" name="route" value="<?php echo $route; ?>">
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
    </div>
<!--    <form method="post" action="http://10.14.18.222:8200/save/am">-->
    <form method="post" action="http://localhost:8200/save/am">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>#</th>
                <!-- <th><input type="checkbox" id="selectAll" value="selectAll"> Select All</th>-->
                <th><input type="checkbox" id="selectAll" value="selectAll"></th>
                <th>
                    <center>Ticket Number</center>
                </th>
                <th>
                    <center>Card Number</center>
                </th>
                <th>
                    <center>Case Type</center>
                </th>
                <th>
                    <center>Cust Name</center>
                </th>
            </tr>
            </thead>
            <tbody>
            <?php
            $val = 'http://10.14.18.222:8081/jw/web/userview/AcctMaintenanceSystem/userviewAcctMaint//795B662514694DA695AACAF9792EF0A3';
            while (($count < $rpp) && ($i < $tcount)) {
                mysql_data_seek($result, $i);
                $data = mysql_fetch_array($result);
                $acct = substr($data['account'], 13);
                $ticketid = $data ['ticketNo'];
                $cardlimit = $data ['cardLimit'];
                $custname = $data ['custName'];
                $cardno = $data ['cardNum'];
                $caseType = $data ['subJnsLaporan'];
                $custph = $data ['custPhone'];
                $custstatus = $data ['cardStatus'];
                $memoCCBM = $data ['memoCCBM'];
                $datamodal = "data-ticket=$ticketid data-cardlimit=$cardlimit data-custname=$custname data-cardno=$cardno data-custph=$custph data-custstatus=$custstatus data-memoccbm=".urlencode($memoCCBM)."";

                ?>
                <tr>
                    <td><?php echo ++$no_urut; ?></td>
                    <td align="center" bgcolor="#FFFFFF"><input name="checkbox2" type="checkbox"
                                                                value="<?php echo $data ['ticketNo']; ?>"></td>
                    <td><a href="#myModal" data-toggle="modal" <?php echo $datamodal; ?>
                           class="pass"><?php echo $data ['ticketNo']; ?></a></td>
                    <td><?php echo $data ['cardNum']; ?></td>
                    <!--<td><?php echo $data ['cardLimit']; ?></td>
						<td><?php echo $data ['cardStatus']; ?></td>-->
                    <td><?php echo $data ['subJnsLaporan']; ?></td>
                    <td><?php echo $data ['custName']; ?></td>
                </tr>
                <?php
                $i++;
                $count++;
            }
            ?>
            <tr>
                <td colspan="6"></td>
            </tr>
            <tr>
                <td colspan="3"><input type="hidden" name="username" value="<?php echo $username; ?>">
                    <input type="hidden" name="name" value="<?php echo $name; ?>">
                    <input type="hidden" name="routedata" value="<?php echo $datatest; ?>">
                    <input type="hidden" name="route" value="<?php echo $route; ?>"></td>
                <?php

                $querys = "select a.userId,b.firstName from dir_user_group a  join dir_user b on a.userId =b.username where a.groupId='$group'";
                $result2 = mysql_query($querys) or die(mysql_error() . "[" . $querys . "]");
                ?>
                <td colspan="2"><select name="assignTo" id="assignTo" class="form-control"
                                        onChange="showUser(this.value)">
                        <option value="">Select</option>
                        <?php
                        while ($row = mysql_fetch_array($result2)) {
                            echo "<option value=" . $row['userId'] . ">" . $row['firstName'] . "(" . $row['userId'] . ")</option>";
                        }
                        ?>
                    </select></td>
                <td><input type="submit" value="Follow Up" class='btn btn-primary'></td>
            </tr>
            </tbody>
        </table>
    </form>
    <div><?php echo paginate_one($reload, $page, $tpages, $rowcount); ?></div>
    <!-- Detail Modal -->
    <div id="myModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Detail Pending List : <input type="text" name="tickno" id="tickno" value=""
                                                                         style="border: none;background: transparent;"/>
                    </h4>
                </div>
                <div class="modal-body">
                    <table width="100%" align="center">
                        <tr>
                            <td width="350">Ticket Number</td>
                            <td>:</td>
                            <td><input type="text" name="tickno" id="tickno" value=""
                                       style="border: none;background: transparent; float:right;"/></td>
                            <td width="350">Card Limit</td>
                            <td>:</td>
                            <td><input type="text" name="cardlim" id="cardlim" value=""
                                       style="border: none;background: transparent;"/></td>
                        </tr>
                        <tr>
                            <td>Card Status</td>
                            <td>:</td>
                            <td><input type="text" name="cardsts" id="cardsts" value=""
                                       style="border: none;background: transparent;"/></td>
                            <td>Customer Name</td>
                            <td>:</td>
                            <td><strong><input type="text" name="custnm" id="custnm" value=""
                                               style="border: none;background: transparent;"/></strong></td>
                        </tr>
                        <tr>
                            <td>Card Number</td>
                            <td>:</td>
                            <td><input type="text" name="cardno" id="cardno" value=""
                                       style="border: none;background: transparent;"/></td>
                            <td>Customer Phone</td>
                            <td>:</td>
                            <td><input type="text" name="custpho" id="custpho" value=""
                                       style="border: none;background: transparent;"/></td>
                        </tr>
                        <tr>
                            <td>Description</td>
                            <td>:</td>
                            <td colspan="4"><textarea type="text" name="description" id="description" value=""
                                       style="border: none;background: transparent;"/></td>
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
    function cls(a) {
        window.opener.location = a;
        window.close();
    }
</script>
</body>
</html>
