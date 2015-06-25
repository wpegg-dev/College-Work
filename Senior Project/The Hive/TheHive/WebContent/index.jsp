<html>
<head>
	<meta name="viewport" content="width=1024" />
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
			<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
	<title>The Hive</title>
	<link rel="stylesheet" type="text/css" href="styles/index.css">
	<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/ui-lightness/jquery-ui.min.css"> 
	<link rel="stylesheet" type="text/css" href="styles/jqueryUi.css">
	
	<!-- GET jQuery Libraries -->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<!-- <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script> -->
	<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
	
	<!-- <link href="https://rawgithub.com/hayageek/jquery-upload-file/master/css/uploadfile.css" rel="stylesheet">
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script src="https://rawgithub.com/hayageek/jquery-upload-file/master/js/jquery.uploadfile.min.js"></script> -->
	
	
	<!-- IMPORT JQUERY FILES TO BUILD VIEWS -->
	<script src="views/ProfileView.js"></script>
	<script src="views/PublicProfileView.js"></script>
	<script src="views/GroupView.js"></script>
	<script src="views/DiscussionView.js"></script>
	<script src="plugins/jqueryFileTree.js"></script>
	<script src="plugins/script.js"></script>
	<script src="plugins/jquery.filedrop.js"></script>
	<script src="plugins/uploadify/jquery.uploadify.js"></script>
	<script src="plugins/jquery.resizecrop-1.0.3.min.js"></script>
	
	<script type="text/javascript">
	
	$(document).ready(function(){				
		$('#processing')
			.hide()
			.append('<img src="images/loading.gif"/>')
			.ajaxStart(function(){
				$(this).show();
			})
			.ajaxStop(function(){
				$(this).hide();
			});
		
		/**************************************************
		/
		/     Header with logo and welcome text
		/
		/**************************************************/
		$('#header')
			.append(
				$('<div/>')
					.attr({'id':'logoDiv'})
					.append(
						$('<img/>')
							.attr({'id':'logo','src':'images/logo.png'})		
					)
			);
		/**************************************************
		/
		/     Nav to welcome
		/
		/**************************************************/
		$('#nav')
			.append(
				$('<div/>')
					.attr({'id':'welcomeText'})
					.append(
						$('<h1/>')
							.text('Welcome to The Hive')
							)
					);
		/**************************************************
		/
		/     Left div for main info
		/
		/**************************************************/
		var _bodyContents = $('#mainBody');
		
		_bodyContents
			.append(
				$('<div/>')
				.attr({'id':'leftMain'})
				.append(
					$('<div/>')
					.attr({'id':'welcomeInfo'})
						.append(
							$('<h1/>')
								.addClass('firstWelcome')
								.text('Manage')
							)
						.append(
							$('<span/>')
								.text('find members and manage your group projects')
							)
						.append(
							$('<h1/>')
								.text('Organize')
							)
						.append(
							$('<span/>')
								.text('organize your files using version control')
							)
						.append(
							$('<h1/>')
								.text('Discuss')
							)
						.append(
							$('<span/>')
								.text('discuss and collaborate easily with the forums')
							)
					)
				)
		
		/**************************************************
		/
		/     Right div for login/sign-up
		/
		/**************************************************/
			.append(
				$('<div/>')
				.attr({'id':'rightMain'})
			//LOGIN
			.append(
				$('<div/>')
				.attr({'id':'login'})
					.append(
						$('<h3/>')
							.text('Login')
							)
					.append(
						$('<div/>')
						.attr({'id':'loginForm'})
							.append(
								$('<span/>')
									.text('Username')
									)
							.append(
								$('<input/>')
									.attr({'id':'unameField','type':'text'})
									)
							.append(
								$('<span/>')
									.text('Password')
									)
							.append(
								$('<input/>')
									.attr({'id':'pwordField','type':'password'})
									.keydown(function(event){
										
										if(event.keyCode === 13){
											event.preventDefault();
											event.stopPropagation();
											userLoginAction();
											return false;
										}
										else{
											this.focus();
										}
									})
									)
							.append(
								$('<input/>')
								.addClass('IndexLoginButton')
								.attr({'id':'loginButton','type':'submit'})
								.val('Login')
								.click(function(){
									userLoginAction();
								})
								)
						)
			)//END LOGIN
			//SIGN UP
			.append(
				$('<div/>')
				.attr({'id':'signup'})
					.append(
						$('<h3/>')
							.text('Or Sign-Up for Free!')
						)
					.append(
						$('<div/>')
						.attr({'id':'signupForm'})
							.append(
								$('<div/>')
								.attr({'id':'formText'})
									.append(
										$('<span/>')
										.text('First Name: ')
										)
									.append(
										$('<span/>')
										.text('Last Name: ')
										)
									.append(
										$('<span/>')
										.text('E-mail: ')
										)
									.append(
										$('<span/>')
										.text('Re-enter E-mail: ')
										)
									.append(
										$('<span/>')
										.text('Password: ')
										)
									.append(
										$('<span/>')
										.text('Re-enter Password: ')
										)
									)
							.append(
								$('<div/>')
								.attr({'id':'formData'})
									.append(
										$('<input/>')
										.attr({'id':'createFirst','type':'text'})
										)
									.append(
										$('<input/>')
										.attr({'id':'createLast','type':'text'})
										)
									.append(
										$('<input/>')
										.attr({'id':'createEmail','type':'text'})
										)
									.append(
										$('<input/>')
										.attr({'id':'createEmailCheck','type':'text'})
										)
									.append(
										$('<input/>')
										.attr({'id':'createPassword','type':'password'})
										)
									.append(
										$('<input/>')
										.attr({'id':'createPasswordCheck','type':'password'})
										)
									// SIGN UP FUNCTION
									.append(
										$('<input/>')
											.addClass('IndexLoginButton')
											.attr({'id':'signUpButton','type':'submit'})
											.val('Sign Up')
											.click(function(){
												
												var _fname = '';
												var _lname = '';
												var _email = '';
												var _emailCheck = '';
												var _pass = '';
												var _passCheck = '';
												
												if($('#createFirst').val() == '')
												{
													alert('Please enter your first name.');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if($('#createLast').val() == '')
												{
													alert('Please enter your last name.');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if($('#createEmail').val() == '')
												{
													alert('Please enter an e-mail address.');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if(($('#createEmail').val()).indexOf('@') <= 0)
												{
													alert('Please enter an e-mail address.\n Email Address must contain an @ symbol');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if(($('#createEmail').val()).indexOf('.com') <= 0 &&
														($('#createEmail').val()).indexOf('.edu') <= 0 &&
														($('#createEmail').val()).indexOf('.net') <= 0 &&
														($('#createEmail').val()).indexOf('.org') <= 0 &&
														($('#createEmail').val()).indexOf('.mail') <= 0)
												{
													alert('Please enter an e-mail address.\n Email Address must end with .com, .gmail, .org, .net, .edu or .mail');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if($('#createEmailCheck').val() == '')
												{
													alert('Please enter an e-mail address.');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if(($('#createEmailCheck').val()).indexOf('@') <= 0)
												{
													alert('Please enter an e-mail address.\n Email Address must contain an @ symbol');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if(($('#createEmailCheck').val()).indexOf('.com') <= 0 &&
														($('#createEmailCheck').val()).indexOf('.edu') <= 0 &&
														($('#createEmailCheck').val()).indexOf('.net') <= 0 &&
														($('#createEmailCheck').val()).indexOf('.org') <= 0 &&
														($('#createEmailCheck').val()).indexOf('.mail') <= 0)
												{
													alert('Please enter an e-mail address.\n Email Address must end with .com, .gmail, .org, .net, .edu or .mail');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if($('#createEmail').val() != $('#createEmailCheck').val())
												{
													alert('Email addresses do not match.');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if($('#createPassword').val() == '')
												{
													alert('Please enter a password.');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if($('#createPasswordCheck').val() == '')
												{
													alert('Please enter a password.');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else if($('#createPassword').val() != $('#createPasswordCheck').val())
												{
													alert('Passwords Do Not Match.');
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												else
												{
													_fname = $('#createFirst').val();
													_lname = $('#createLast').val();
													_email = $('#createEmail').val();
													_emailCheck = $('#createEmailCheck').val();
													_pass = $('#createPassword').val();
													_passCheck = $('#createPasswordCheck').val();
													
													
													$.ajax({ 
														url:"/TheHive/UserLoginService",
														data:{
															"firstName": _fname,
															"lastName": _lname,
															"emailAddress": _email,
															"password": _pass,
															"method":"signUp"
														},
														type:'POST',
														dataType:"json",
														success: function(html){
															$('#globalSettings').data('userData',html);
															loadProfile(html['userData']);
															
														},
														error: function(html){
															alert("Email address already in use. Please try again.");
														}
													}); //Closes ajax
													
													$('#createFirst').val('');
													$('#createLast').val('');
													$('#createEmail').val('');
													$('#createEmailCheck').val('');
													$('#createPassword').val('');
													$('#createPasswordCheck').val('');
												}
												
												return false;
											})
									)//END SIGN UP FUNCTION
								)
							)
						)//END SIGN UP
				);
		
/************************END MAIN*************************/
	
		if(!Array.prototype.filter) {
		    Array.prototype.filter = function (fun) {
		        "use strict";

		        if ((this === void(0)) || (this === null))
		        {
		            throw new TypeError();
		        }

		        var t = Object(this);
		        var len = t.length >>> 0;
		        if (typeof fun !== "function")
		            throw new TypeError();

		        var res = [];
		        var thisp = arguments[1];
		        for (var i = 0; i < len; i++) {
		            if (i in t) {
		                var val = t[i]; // in case fun mutates this
		                if (fun.call(thisp, val, i, t))
		                    res.push(val);
		            }
		        }

		        return res;
		    };
		}
	});//Closes $(document).ready(
	
	function loadProfile(userData)
	{
		var _defaultProfilePic = 'http://www.csehive.com/'+userData[0]['Profile Picture'];
		
		if(userData.length === 0)
		{
			alert('Invalid username and/or password combination. Please try again.');
		}
		else
		{
			$('#mainBody').ProfileView({'FirstName':userData[0]['First Name'],'PersonId':userData[0]['Person ID'],'LastName':userData[0]['Last Name'],'emailAddress':userData[0]['Email Address'],'userRepoLocation':userData[0]['User Repository'],'profilePic':_defaultProfilePic});
		}
		
	} 
	
	function userLoginAction()
	{
		var _uname = '';
		var _pword = '';
											
		if($('#unameField').val() == '')
		{
			alert('Please Enter Your Username.');
			$('#unameField').val('');
			$('#pwordField').val('');
		}
		else if($('#pwordField').val() == '')
		{
			alert('Please Enter Your Password.');
			$('#unameField').val('');
			$('#pwordField').val('');
		}
		else
		{
			_uname = $('#unameField').val();
			_pword = $('#pwordField').val();
			
			$('#unameField').val('');
			$('#pwordField').val('');
			
			$.ajax({ 
				url:"/TheHive/UserLoginService",
				data:{
					"emailAddress": _uname,
					"password": _pword,
					"method":"signIn"
				},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('userData',html);
					loadProfile(html['userData']);
					//alert("it worked");
				},
				error: function(html){
					alert("Invalid username/password combination." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		
				return false;
												
			}
	}
	
	
	
	</script>
	
</head>
<body>
	<!-- Holds Data on page -->
	<div id="globalSettings" style="display: none;"></div>

	<div id="header"></div>
	<div id="links"></div>
	<div id="nav"></div>
	<div id="mainBody">
		<div id="processing">
		</div>
		<div id="welcome">
		</div>
	</div>
	<div id="footer">
	</div>
</body>
</html>