<?php
include 'database.php';
if (!empty($_POST)){
	$usernameError = null;
	$groupError = null;

	$username = $_POST['username'];
	$group = $_POST['group'];

	$valid = true;
	if(empty($username)){
		$usernameError = 'Masukkan username';
		$valid = false;
	}
	if(empty($group)){
		$groupError = 'Pilih Salah Satu Group';
		$valid = false;
	}
	if ($valid){
		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$sql = "INSERT INTO dir_user_group (groupId,userId) values(?, ?)";
			$q = $pdo->prepare($sql);
			$q->execute(array($username,$groupId));
			Database::disconnect();
			header("Location: index.php");
	}
}

?>

<!DOCTYPE>
<html>
<head>
	<title>Group Admin</title>
	<meta charset="utf-8">
    <link   href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
	<div class="span10 offset1">
    	<div class="row">
			<h3>Create a User</h3>
		</div>
    		
	    <form class="form-horizontal" action="create.php" method="post">
			<div class="control-group <?php echo !empty($usernameError)?'error':'';?>">
					    <label class="control-label">Username</label>
					    <div class="controls">
					      	<input name="username" type="text"  placeholder="Username" value="<?php echo !empty($username)?$username:'';?>">
					      	<?php if (!empty($usernameError)): ?>
					      		<span class="help-inline"><?php echo $usernameError;?></span>
					      	<?php endif; ?>
					    </div>
					  </div>
					  <div class="control-group <?php echo !empty($groupError)?'error':'';?>">
					    <label class="control-label">Group</label>
					    <div class="controls">
					      	<input name="group" type="text" placeholder="Group" value="<?php echo !empty($group)?$group:'';?>">
					      	<?php if (!empty($groupError)): ?>
					      		<span class="help-inline"><?php echo $groupError;?></span>
					      	<?php endif;?>
					    </div>
					  </div>
				
					  <div class="form-actions">
						  <button type="submit" class="btn btn-success">Create</button>
						  <a class="btn" href="index.php">Back</a>
						</div>
					</form>
				</div>
	
</div>

</body>
</html>