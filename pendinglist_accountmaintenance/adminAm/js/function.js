// JavaScript Document
function choosebatch(){
	var btch = document.getElementById("batch").value;
	var srch = document.getElementById("srchby");
	var equal = document.getElementById("equalqry");
	var btns = document.getElementById("btn");
	if (btch!="")
	{
		equal.style.display="block";
		srch.style.display="block";
		//equal.value = "Isikan "+srch;
		btns.style.display="block";
	}
	else
	{
		equal.style.display="none";
		srch.styke.display="none";
		//equal.value = "";
		btns.style.display="none";
	}
}

function choose(){
	var srch = document.getElementById("srchby").value;
	var equal = document.getElementById("equalqry");
	var btns = document.getElementById("btn");
	//alert(srch);
	if (srch!="")
	{
		equal.style.display="block";
		if (srch == "TanggalBayar"){
			equal.placeholder = "Format Tanggal yyyy-mm-dd (2016-03-14)";
		}else{
			equal.placeholder = "Isikan "+srch;
		}
		//equal.value = "Isikan "+srch;
		btns.style.display="block";
	}
	else
	{
		equal.style.display="none";
		//equal.value = "";
		btns.style.display="none";
	}
}

function choosemdl(){
	var srch = document.getElementById("srchbymdl").value;
	var equal = document.getElementById("equalqrymdl");
	var daterange1 = document.getElementById("equalqrymddate1");
	var daterange2 = document.getElementById("equalqrymddate2");
	var btns = document.getElementById("btnmdl");
	if (srch!="")
	{
		if (srch=="TanggalBayar"){
			daterange1.placeholder = "Format ex : 2016-03-14";
			daterange2.placeholder = "Format ex : 2016-03-14";
			daterange1.style.display="block";
			daterange2.style.display="block";
			equal.style.display="none";
			btns.style.display="block";
		}else{
			daterange1.style.display="none";
			daterange2.style.display="none";
			equal.style.display="block";
			btns.style.display="block";
		}
	}
	else
	{
		daterange1.style.display="none";
		daterange2.style.display="none";
		equal.style.display="none";
		//equal.value = "";
		btns.style.display="none";
	}
}

function choosehis(){
	var srch = document.getElementById("srchbyhis").value;
	var equal = document.getElementById("equalqryhis");
	var btns = document.getElementById("btnhis");
	if (srch!="")
	{
		equal.style.display="block";
		equal.style.display="block";
		if (srch == "TanggalBayar"){
			equal.placeholder = "Format Tanggal yyyy-mm-dd (2016-03-14)";
		}else{
			equal.placeholder = "Isikan "+srch;
		}
		//equal.value = "Isikan "+srch;
		btns.style.display="block";
		btns.style.display="block";
	}
	else
	{
		equal.style.display="none";
		//equal.value = "";
		btns.style.display="none";
	}
}