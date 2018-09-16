<!DOCTYPE html>
<html>
    <head>
        <title>Reporting Account Maintenance</title>
        <link rel="stylesheet" href="bootstrap.min.css"/>
		<style>
		table {
			border-collapse: collapse;
			width: 90%;
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
               <li><a href="accounMaintenanceLimit_HistoryAll.php">History ALL</a></li>
			   <li class="active"><a href="export.php">Report Data</a></li>
               </ul>
          </div>
     </div>
</nav>
		<div class="container">
  	<p><strong><i class="fa fa-file-excel-o"></i>&nbsp;&nbsp;&nbsp;Exporting ACCOUNT MAINTENANCE</strong></p>
    <form id="myForm" action="transmission.php" method="post">
  	<table>
		<tr>
            <td><strong>Export</strong> </td>
            <td>:</td>
            <td><select name="jenisexport" id="jenisexport" class="form-control" onChange="choose();">
                <option value="">Select</option>
                <option value="daily">Daily Report</option>
                <option value="sid">SID Report</option>
                </select></td>
        </tr>
    	<tr>
            <td><strong>Type File</strong> </td>
            <td>:</td>
            <td><select name="tipeexport" id="tipeexport" class="form-control">
                <option value="">Select</option>
                <option value="excel">Excel Report</option>
                <option value="pdf">PDF Report</option>
                </select></td>
        </tr>
        <tr>
            <td><strong></strong> </td>
            <td></td>			
            <td><select name="srchbymdl" id="srchbymdl" class="form-control" style="display:none" onChange="choosemdl();">
                <option value="">Select</option>
				<option value="all">All Request</option>
                <option value="dateCreated">DATE CREATED</option>
				<option value="c_date4B">DATE DECISION HEAD ANALYST</option>
                <option value="c_txtCardNo">NO KARTU</option>
                <option value="c_fldApprAnalyst">APPROVE OLEH ANALIS</option>
                <option value="c_fldStatApprHeadAnalyst">APPROVE OLEH HEAD ANALIS</option>
                <option value="c_txtNoTicket">NOMOR TIKET</option>                
                </select></td>
            <td><input type="text" name="equalqrymdl" id="equalqrymdl" class="form-control" style="display:none;"/></td>
			<td><input type="text" name="equalqrymddate1" id="equalqrymddate1" class="form-control" style="display:none;"/></td>
            <td><input type="text" name="equalqrymddate2" id="equalqrymddate2" class="form-control" style="display:none;"/></td>
			<td><input type="text" name="equalqrymddate3" id="equalqrymddate3" class="form-control" style="display:none;"/></td>
            <td><input type="text" name="equalqrymddate4" id="equalqrymddate4" class="form-control" style="display:none;"/></td>
            <td><input type="submit" name="btnmdl" id="btnmdl" class="btn btn-primary" value="Export" style="display:none;"></td>
        </tr>
    </table>
    </form>
	</div>
 </body>
   <script src="js/jquery.min.js"></script>
		<script src="js/jquery-1.12.4.js"></script>
		<script src="js/jquery-ui.js"></script>
	    <script >
			$( function() {
			$( "#equalqrymddate1" ).datepicker({ dateFormat: 'yy/m/dd' });
			});
			 $( function() {
		$( "#equalqrymddate2" ).datepicker({ dateFormat: 'yy/m/dd' });
			});
			$( function() {
			$( "#equalqrymddate3" ).datepicker({ dateFormat: 'yy-mm-dd' });
			});
			 $( function() {
		$( "#equalqrymddate4" ).datepicker({ dateFormat: 'yy-mm-dd' });
			});
			
	function choose(){
	var btn = document.getElementById("btnmdl");
			var tes = document.getElementById("jenisexport").value;
			var srch1 = document.getElementById("srchbymdl");
			console.log(tes);
			if (tes=="daily"){
			btn.style.display="block";
			srch1.style.display="block";
			 document.getElementById("myForm").action = "transmission.php";
			}else{
			srch1.style.display="none";
			btn.style.display="block";
			 document.getElementById("myForm").action = "transmission2.php";
			}
			}
			//
		function choosemdl(){
		 
			var srch = document.getElementById("srchbymdl").value;
			var equal = document.getElementById("equalqrymdl");
			var daterange1 = document.getElementById("equalqrymddate1");
			var daterange2 = document.getElementById("equalqrymddate2");
			var daterange3 = document.getElementById("equalqrymddate3");
			var daterange4 = document.getElementById("equalqrymddate4");
			var btns = document.getElementById("btnmdl");
			//alert(srch);
			if (srch!="")
			{
			var d = new Date();
			var year = d.getFullYear();
			var day = d.getDay();
			var m = d.getMonth();
				if (srch=="dateCreated"){
				daterange1.placeholder = "Format ex : "+year+"-"+m+"-"+day;
				daterange2.placeholder = "Format ex : "+year+"-"+m+"-"+day;
				daterange1.style.display="block";
				daterange2.style.display="block";
				daterange3.style.display="none";
				daterange4.style.display="none";
				equal.style.display="none";
				btns.style.display="block";
			}else if (srch=="c_date4B"){
				daterange3.placeholder = "Format ex : "+year+"-"+m+"-"+day;
				daterange4.placeholder = "Format ex : "+year+"-"+m+"-"+day;
				daterange3.style.display="block";
				daterange4.style.display="block";
				daterange1.style.display="none";
				daterange2.style.display="none";
				equal.style.display="none";
				btns.style.display="block";
			
			}else if (srch=="all"){
				daterange1.style.display="none";
				daterange2.style.display="none";
				equal.style.display="none";
				btns.style.display="block";
			
			}
			else{
				daterange1.style.display="none";
				daterange2.style.display="none";
				equal.style.display="block";
				btns.style.display="block";
			}
		}else{
			daterange1.style.display="none";
			daterange2.style.display="none";
			equal.style.display="none";
		//equal.value = "";
			btns.style.display="none";
		}
	}
	</script>
 </html>