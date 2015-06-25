(function($) {
	var methods = {
			"init": function(options){
				$.extend( $.profile.global, options );
				return this.each(function(){
					$.profile.create();
				});
			}
		};

	/* *****************************
	 * 
	 * create namespace
	 *
	 ******************************/
	$.profile = {};
	
	/* *********************************
	 * 
	 *  create global variables
	 *
	 * *********************************/
	$.profile.global = {
		"emailAddress": null,
		"FirstName": null,
		"LastName": null,
		"PersonId": null,
		"currentRequest": null,
		"profilePic": null,
		"userRepoLocation":null
		
	};
	
	/* ************************************
	 * 
	 *  declare method to build view from jsp
	 * 
	 * ************************************/
	$.fn.ProfileView = function( method ) {	    
		// Method calling logic
		if ( methods[method] ) {
			return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
		} else if ( typeof method === 'object' || ! method ) {
			return methods.init.apply( this, arguments );
		} else {
			alert($.error( 'Method ' +  method + ' does not exist on jQuery.dialogPrompt' ));
			$.error( 'Method ' +  method + ' does not exist on jQuery.dialogPrompt' );
		}
	};
	
	/* **********************************
	 * 
	 *  create destroy function
	 * 
	 * **********************************/
	$.profile.destroy = function(){
		if($.profile.global.currentRequest!==null)
		{
			$.profile.global.currentRequest.abort();
		}
		
		if($.profile.global.container !== null)
		{
			$.profile.global.container.remove();
		}
		
		$('#mainBody').ProfileView("destroy");
	};
	
	/* **********************************
	 * 
	 *  create create function
	 * 
	 * **********************************/
	$.profile.create = function()
	{
		$.profile.global.containingDiv = $('<div/>').attr('id','userProfile');
		
		$.profile.buildView();
	};
	
	/* ************************************
	 * 			BUILD VIEW  
	 * ************************************/
	$.profile.buildView = function()
	{	
		//remove unwanted info from view
		$.profile.destroyLoginScreen();
		
		//get the groups the user is in
		$.profile.gatherGroups();
		
		//select div's to which info will be appended
		var _linkDiv = $('#links');
		var _navBar = $('#nav');
				
				//attach profile picture and username to navigation
				_navBar
					.append(
						$('<img/>')
							.attr({'id':'profilePicture','src':$.profile.global.profilePic})
					)
					.append(
						$('<div/>')
							.attr({'id':'username'})
							.append(
								$('<label/>')
									.attr({'id':'UsersName'})
									.text($.profile.global.FirstName+" "+$.profile.global.LastName)
							)
							
					);
				
				
				//create div to hold users groups
				var _groupsDiv = 
						$('<div/>')
							.attr({'id':'groups'});
				
				//create div to hold users files
				var _myFilesDiv =
					$('<div/>')
						.attr({'id':'myFilesDiv'});
				
				//div where view body will be appended
				var _containingDiv = $('#welcome');
				
				//append profile and files div to body
				_containingDiv
					.append(
						$('<div/>')
							.attr({'id':'profileDiv'})
							.append(
								_groupsDiv		
							)
					.append(
						$('<div/>')
							.attr({'id':'filesDiv'})
							.append(
								$('<div/>')
									.attr({'id':'fileActionDiv'})
									.append(
										$('<h2/>')
											.attr({'id':'repoHead'})
											.text("Repository")
											)
									.append(
										$('<table/>')
											.attr({'id':'fileActionBar'})
											.append(
												$('<tr/>')
													.append(
														$('<td/>')
															.attr({'id':'uploadFileLink'})
															.append(
																$('<div/>')
																	.attr({'id':'uploadFileLink'})
																	.append(
																		$('<label/>')
																		.text('Upload')
																	)
															)
															.click(function(){
																var _fullUserRepo = '/var/www/UserRepo/'+$.profile.global.userRepoLocation;
																$.profile.uploadUserFile(_fullUserRepo);
															})
													)
											)
									)
							)
							.append(
									_myFilesDiv	
							)
					)
				);
				
				//append site navigation to navbar
				_linkDiv
					.append(
						$('<div/>')
							.attr({'id':'navBar'})
							.append(
								$('<ul/>')
									.append(
										$('<li/>')
											.append(
												$('<label/>')
												.text("Profile")
												.click(function(){
													//build users profile view when clicked
													$.profile.buildView();
													})
												)
											)
									.append(
										$('<li/>')
										.append(
												$('<label/>')
												.text("Groups")
												.click(function(){
													//build users groups view when clicked
													$.profile.buildMyGroups();
													})
												)
										)
									.append(
										$('<li/>')
										.append(
												$('<label/>')
												.text("Friends")
												.click(function(){
													//build users friends view when clicked
													$.profile.buildMyFriends();
													})
												)
										)
									.append(
										$('<li/>')
										.append(
												$('<label/>')
												.text("Settings")
												.click(function(){
													//build users settings view when clicked
													$.profile.buildSettings();
													})
												)
										)
								)
						);
				
				//append search functionality to nav bar
				_linkDiv
					.append(
						$('<form/>')
							.attr({'id':'searchBar'})
							.append(
								$('<input/>')
									.attr({'type':'text','name':'search','id':'search'})
									.keydown(function(event){
										//allow user to hit ENTER to perform search
										if(event.keyCode === 13){
											event.preventDefault();
											event.stopPropagation();
											$.profile.searchAction();
											return false;
										}
										else{
											this.focus();
										}
									})
							)
							.append(
								$('<img/>')
									.attr({'id':'searchSubmit','src':'images/searchIcon.png'})
									.click(function(){
										//perform search for user
										$.profile.buildSearchResults();
									})
							)
					);
				
				//call jquery plugin to build repository view
				$('#myFilesDiv').fileTree({
					root:'/var/www/UserRepo/'+$.profile.global.userRepoLocation,
					script:'plugins/jqueryFileTree.jsp'},
					function(file){
						e.preventDefault();  
						
						var _stringToRemove = '/var/www/';
						var _fileloc = file.replace(_stringToRemove,'');
						
						window.location.hre = _fileloc;
					});
				
				//resize the users profile picture to 108x108
				$.profile.cropProfilePic();
	};
	
	/* *******************************
	 * 
	 *  declare action for search
	 * 
	 * *******************************/
	$.profile.searchAction = function(){
		$("#searchSubmit").click();
	};
	
	/* ******************************************************
	 * 
	 *  build div which will show the groups the user is in
	 * 
	 * ******************************************************/
	$.profile.loadGroups = function(groupData){
		
		var _groupDiv = $('#groups');
		var _displayData = groupData;
		
		//append group heading and beginning of group list
		_groupDiv
			.append(
				$('<h2/>')
					.text('My Groups')
					.attr('id','groupListHead')
			)
			.append(
				$('<ul/>')
					.attr('id','groupList')
			);
		
		//loop through all users groups and append the groupList
		for(var i=0;i<_displayData.length;i++)
		{
			var _curData = _displayData[i];
			
			$('#groupList')
				.append
				(
					$('<li/>')
						.append(
							$('<label/>')
								.attr({'id':_curData['Group ID']})
								.addClass('groupsLink')
								.text(_curData['Group Name'])
								//action taken when a group is clicked
								.click(function(){$.profile.clickGroup($(this));})
						)
				); 
		}		
	};
	
	/* ******************************************************
	 * 
	 *  get the group id of the group which was clicked on 
	 *  from the My Groups div
	 * 
	 * ******************************************************/
	$.profile.clickGroup = function(link){
		$('#mainBody').GroupView({'groupId':link.attr('id')});
	};
	
	/* ******************************************************
	 * 
	 *  remove unwanted data from view
	 * 
	 * ******************************************************/
	$.profile.destroyLoginScreen = function(){
		$('#welcomeText').remove();
		$('#rightMain').remove();
		$('#leftMain').remove();
		$('#profileDiv').remove();
		$('#searchBar').remove();
		$('#username').remove();
		$('#profilePicture').remove();
		$('#groupProfileUpload').remove();
		$('#groupsRight').remove();
		$('#groupSettingsDiv').remove();
		$('#groupsLeft').remove();
		$('#navBar').remove();
		$('#groupPicture').remove();
		$('#MyGroupsPage').remove();
		$('#editMembers').remove();
		$('#searchResultsPage').remove();
		$('#MyFriends').remove();
		$('#settingsList').remove();
		$('#settingsView').remove();
		$('#commentBar').remove();
		$('#userProfileUpload').remove();
		$('#adminSettings').remove();
		$('#commentsDiv').remove();
		$('#connectToFriendsBar').remove();
	};
	
	/* ***********************************
	 * 
	 * function to get the simple group info for profile page
	 * 
	 * ***********************************/
	$.profile.gatherGroups = function(){
		//set the current page request to following
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/UserLoginService",
		  	  	data:{
		  	  		"personId": $.profile.global.PersonId,
		  	  		"method":"getGroups"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					//put data into DOM
					$('#globalSettings').data('simpleGroupData',html);
					$.profile.global.simpleGroups = html['simpleGroupData'];
					
					//reload the users groups
					$.profile.loadGroups(html['simpleGroupData']);
				},
				error: function(html){
					alert("An error occured while gathering group information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		//return false;
	};
	
	/* *************************************************
	 *
	 * function to build page to show user their groups
	 * 
	 * *************************************************/
	$.profile.buildMyGroups = function(){
		
		//remove unwanted info from view
		$.profile.destroyLoginScreen();
		$('#welcome').show();
		
		var _displayData = $.profile.global.simpleGroups;
		var _linkDiv = $('#links');
		var _navBar = $('#nav');
		
		//append profile picture and username to nav bar
		_navBar
			.append(
				$('<img/>')
					.attr({'id':'profilePicture','src':$.profile.global.profilePic})
			)
			.append(
				$('<div/>')
					.attr({'id':'username'})
					.append(
						$('<label/>')
							.attr({'id':'UsersName'})
							.text($.profile.global.FirstName+" "+$.profile.global.LastName)
					)
					
			);
		
		//append navigation links to nav bar
		_linkDiv
			.append(
				$('<div/>')
					.attr({'id':'navBar'})
					.append(
						$('<ul/>')
							.append(
								$('<li/>')
									.append(
										$('<label/>')
										.text("Profile")
										.click(function(){
											//build users profile view
											$.profile.buildView();
											})
										)
									)
							.append(
								$('<li/>')
								.append(
										$('<label/>')
										.text("Groups")
										.click(function(){
											//build users groups view
											$.profile.buildMyGroups();
											})
										)
								)
							.append(
								$('<li/>')
								.append(
										$('<label/>')
										.text("Friends")
										.click(function(){
											//build users friends view
											$.profile.buildMyFriends();
											})
										)
								)
							.append(
								$('<li/>')
								.append(
										$('<label/>')
										.text("Settings")
										.click(function(){
											//build users settings view
											$.profile.buildSettings();
											})
										)
								)
						)
				)
				.append(
					$('<form/>')
						.attr({'id':'searchBar'})
						.append(
						$('<input/>')
							.attr({'type':'text','name':'search','id':'search'})
							.keydown(function(event){
								
								if(event.keyCode === 13){
									event.preventDefault();
									event.stopPropagation();
									$.profile.searchAction();
									return false;
								}
								else{
									this.focus();
								}
							})
					)
					.append(
						$('<img/>')
							.attr({'id':'searchSubmit','src':'images/searchIcon.png'})
							.click(function(){
								//build search results when clicked
								$.profile.buildSearchResults();
							})
					)
				);
		
		//append groups to view
		$('#welcome')
			.append(
				$('<div/>')
					.attr({'id':'MyGroupsPage'})
			);
		
		var _groupDiv =  $('#MyGroupsPage');
			
		//append groups header and beginning of group list
		_groupDiv
			.append(
				$('<h2/>')
					.text('My Groups')
					.attr('id','groupListHead')
			)
			.append(
				$('<ul/>')
					.attr('id','groupList')
			);
		
		//append button to create a new group
		$('#groupList')
			.append(
				$('<label/>')
					.attr({'id':'final'})
					.text('Create Group')
					.click(function(){
						//call function to create the group
						$.profile.createNewGroup();
						
						//put div into a dialog box which pops up
						$('#createGroupForm').dialog({
							'height': '320',
							'width': '600',
							'modal':'false',
							'closeOnEscape': 'true',
				            'resizable': 'false',
				            'close': function(ev, ui) { 
				            	//rebuild groups after new group created
				            	$.profile.buildMyGroups();
				            	$(this).remove();
				            }
						})
						.show();
					})
				)
			.append(
				$('<br>')	
				);
		
			//loop through users groups and append each to groupList
			for(var i=0;i<_displayData.length;i++)
			{
				var _curData = _displayData[i];
				
				$('#groupList')
					.append
					(
						$('<li/>')
							.append(
								$('<label/>')
									.attr({'id':_curData['Group ID']})
									.addClass('groupsLink')
									.text(_curData['Group Name'])
									.click(function(){$.profile.clickGroup($(this));})
							)
					); 
			}
			
			//resize picture to 108x108
			$.profile.cropProfilePic();
			
	};
	
	/* ***************************************
	 * 
	 *  function to build settings view
	 * 
	 * ***************************************/
	$.profile.buildSettings = function(){
		
		//remove unwanted into from view
		$('#welcomeText').remove();
		$('#rightMain').remove();
		$('#leftMain').remove();
		$('#profileDiv').remove();
		$('#groupsRight').remove();
		$('#groupsLeft').remove();
		$('#MyGroupsPage').remove();
		$('#searchResultsPage').remove();
		$('#groupProfileUpload').remove();
		$('#MyFriends').remove();
		$('#groupSettingsDiv').remove();
		$('#connectToFriendsBar').remove();
		$('#settingsView').remove();
		$('#userProfileUpload').remove();
		$('#editMembers').remove();
		$('#settingsView').remove();
		$('#fileuploader').dialog('destroy').remove();
		
		//append settings page to view
		$('#welcome')
		.append(
			$('<div/>')
				.attr({'id':'settingsView'})
				.append(
					$('<h2/>')
						.attr({'id':'settingsHead'})
						.text('Edit Settings')
					)
				.append(
					$('<h3/>')
						.text('Click here to update your profile picture.')
						.click(function(){
							//build view to upload a new profile picture
							$.profile.uploadProfilePicture();
						})
				)
				.append(
					$('<h3/>')
						.text('Double-click on a box to edit')
					)
				.append(
					$('<div/>')
						.attr({'id':'leftSettings'})
						.append(
							$('<ul/>')
								.append(
									$('<li/>')
										.append(
											$('<label/>')
												.text('First Name:')
											)
									)
								.append(
									$('<li/>')
										.append(
											$('<label/>')
												.text('Last Name:')
											)
									)
								.append(
									$('<li/>')
										.append(
											$('<label/>')
												.text('E-mail Address:')
											)
									)
								.append(
									$('<li/>')
										.append(
											$('<label/>')
												.text('Password:')
											)
									)
								.append(
									$('<li/>')
										.append(
											$('<label/>')
												.text('Re-enter Password:')
											)
									)
							)
					)
				.append(
					$('<div/>')
						.attr({'id':'rightSettings'})
						.append(
							$('<ul/>')
								.append(
									$('<li/>')
										.append(
											$('<input/>')
												.attr({'id':'fNameInput','type':'text','readonly':'readonly'})
												.val($.profile.global.FirstName)
												.dblclick(function(){
													//toggle readonly attribute on field
													$(this).removeAttr('readonly');
													$(this).css({'color':'#000000'});
												})
											)
									)
								.append(
									$('<li/>')
										.append(
											$('<input/>')
												.attr({'id':'lNameInput','type':'text','readonly':'readonly'})
												.val($.profile.global.LastName)
												.dblclick(function(){
													//toggle readonly attribute on field
													$(this).removeAttr('readonly');
													$(this).css({'color':'#000000'});
												})
											)
									)
								.append(
									$('<li/>')
										.append(
											$('<input/>')
												.attr({'id':'emailAddInput','type':'text','readonly':'readonly'})
												.val($.profile.global.emailAddress)
												.dblclick(function(){
													//toggle readonly attribute on field
													$(this).removeAttr('readonly');
													$(this).css({'color':'#000000'});
												})
											)
									)
								.append(
									$('<li/>')
										.append(
											$('<input/>')
												.attr({'id':'passwordInput','type':'password','readonly':'readonly'})
												.val('')
												.dblclick(function(){
													//toggle readonly attribute on field
													$(this).removeAttr('readonly');
													$(this).css({'color':'#000000'});
												})
											)
									)
								.append(
									$('<li/>')
										.append(
											$('<input/>')
												.attr({'id':'rePasswordInput','type':'password','readonly':'readonly'})
												.val('')
												.dblclick(function(){
													//toggle readonly attribute on field
													$(this).removeAttr('readonly');
													$(this).css({'color':'#000000'});
												})
											)
									)
								)
							)
				.append(
					$('<div/>')
						.attr({'id':'clearfix'})
					)
				.append(
					$('<button/>')
						.attr({'id':'final'})
						.text('Save Changes')
						.css({'width':'130','font-size':'13px'})
						.click(function(){
							
							//verify that information input is allowed
							var _newFName = $('#fNameInput').val();
							var _newLName = $('#lNameInput').val();
							var _newEmail = $('#emailAddInput').val();
							var _newPass = $('#passwordInput').val();
							var _newPassCheck = $('#rePasswordInput').val();
							
							if(_newFName == '')
							{
								alert('Please Enter Your First Name.');
							}
							else if(_newLName == '')
							{
								alert('Please Enter Your Last Name.');
							}
							else if(_newEmail == '')
							{
								alert('Please Enter Your Email Address.');
							}
							else if(_newPass != '' || _newPassCheck != '')
							{
								if(_newPass === _newPassCheck)
								{
									//if new password matches new password check and is not null updated account
									$.profile.updateSettingsWithPassword(_newFName,_newLName,_newEmail,_newPass,_newPassCheck);
								}
								else
								{
									alert('New Passwords do not match. Please Try again');
								}
							}
							else
							{
								//update users account assuming that the password is not being updated
								$.profile.updateSettings(_newFName,_newLName,_newEmail);
							}
						})
					)
			);	
	};
	
	/* *************************************
	 * 
	 *  function to create search results
	 * 
	 * *************************************/
	$.profile.buildSearchResults = function(){
		
		//replace info in nav bar with logged in users info
		$.profile.userActionsRebuildNav();
		
		//call function to get search results
		$.profile.getSearchResults($('#search').val());
		
		$('#processing').show();
		
		//set timer to allow for data to return from database
		setTimeout(function()
		{
			//remove unwanted info from view
			$('#processing').hide();
			$('#profileDiv').remove();
			$('#searchResultsPage').remove();
			$('#groupSettingsDiv').remove();
			$('#MyFriends').remove();
			$('#groupsRight').remove();
			$('#groupLeft').remove();
			$('#settingsView').remove();
			$('#groupProfileUpload').remove();
			$('#settingsView').remove();
			$('#MyGroupsPage').remove();
			$('#editMembers').remove();
			$('#groupsLeft').remove();
			
			//append search header and beginning of resultsList
			$('#welcome')
				.append(
					$('<div/>')
						.attr({'id':'searchResultsPage'})
						.append(
							$('<h2/>')
								.attr({'id':'groupListHead'})
								.text('Search Results')
						)
						.append(
							$('<ul/>')
								.attr({'id':'searchResultList'})
						)
				);
		
			//set _displayData to data returned from search function
			var _displayData = $.profile.global.searchResults;
			
			//loop through results and append them to searchResultList
			for(var i=0;i<_displayData.length;i++)
			{
				var _curData = _displayData[i];
				//var _id = _curData['Group ID'];
				
				$('#searchResultList')
					.append
					(
						$('<p/>')
							.append(
								$('<li/>')
								.append(
									$('<img/>')
										.attr({'id':'searchProfilePicture','src':'http://www.csehive.com/'+_curData['Searched Item Profile Picture']})
										.addClass('searchProfilePicture')
								)
								.append(
									$('<label/>')
										.attr({'id':_curData['Searched Item ID'],'name':_curData['Searched Item Type']})
										.addClass(_curData['Searched Item Type'])
										.css({'font-weight':'bold'})
										.text('       '+_curData['Searched Item Name'])
										.click(function(){$.profile.clickSearch($(this));})
								)
							)
					); 
			}
			
			//resize pictures to 108x108
			$.profile.cropProfilePic();
			
		},2000);
		
	};
	
	/* *************************************************
	 * 
	 *  function to build person or group view depending
	 *  on which the user has selected
	 * 
	 * *************************************************/
	$.profile.clickSearch = function(link){
		
		if(link.attr('name') === 'Group')
		{
			$('#mainBody').GroupView({'groupId':link.attr('id')});
		}
		if(link.attr('name') === 'Person')
		{
			$('#mainBody').PublicProfileView({'PublicPersonId':link.attr('id')});
		}
	};
	
	/* *************************************
	 * 
	 *  function to gather search data
	 * 
	 * *************************************/
	$.profile.getSearchResults = function(keyword){
		
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/SearchDataService",
		  	  	data:{
		  	  		"keyword": keyword,
		  	  		"method":"search"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('searchResults',html);
					$.profile.global.searchResults = html['searchResults'];
				},
				error: function(html){
					alert("An error occured while gathering search information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		
	};
	
	/* *************************************
	 * 
	 *  function to create a new group
	 * 
	 * *************************************/
	$.profile.createNewGroup = function(){
		
		//create dialog box to allow user to create a new group
		$('#welcome')
			.append(
				$('<div/>')
					.attr({'id':'createGroupForm','title':'Create New Group'})
					.append(
						$('<table/>')
							.attr({'id':'createGroupTable'})
							.append(
								$('<tr/>')
									.append(
										$('<td/>')
											.append(
												$('<label/>')
													.text('Group Name: ')
											)
									)
									.append(
										$('<td/>')
											.append(
												$('<input/>')
													.attr({'id':'gNameInput','type':'text','max-length':'45'})
											)
									)
							)
							.append(
								$('<tr/>')
									.append(
										$('<td/>')
											.append(
												$('<label/>')
													.text('Course Number: ')
											)
									)
									.append(
										$('<td/>')
											.append(
												$('<input/>')
													.attr({'id':'courseNumInput','type':'text','max-length':'45'})
											)
									)
							)
							.append(
								$('<tr/>')
									.append(
										$('<td/>')
											.append(
												$('<label/>')
													.text('Group Description: ')
											)
									)
									.append(
										$('<td/>')
											.append(
												$('<textarea/>')
													.attr({'id':'gDescrInput','rows':'5','cols':'50','resizable':'none'})
											)
									)
							)
							.append(
								$('<tr/>')
									.append(
										$('<td/>')
									)
									.append(
										$('<td/>')
											.append(
												$('<button/>')
													.attr({'id':'submitCreateGroup'})
													.val('Create Group')
													.text('Create Group')
													.click(function(){
														var _gName = $('#gNameInput').val();
														var _cName = $('#courseNumInput').val();
														var _descr = $('#gDescrInput').val();
														
														//call function to create new group
														$.profile.createGroup(_gName,_cName,_descr);
														
													})
											)
									)
							)
					)
			)
			.hide();
		
	};
	
	/* **************************************
	 * 
	 *  function to create new group
	 * 
	 * **************************************/
	$.profile.createGroup = function(groupName,courseTitle,groupDescription){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/GroupDataService",
		  	  	data:{
		  	  		"personId":$.profile.global.PersonId,
		  	  		"groupName": groupName,
		  	  		"courseTitle":courseTitle,
		  	  		"gDescr":groupDescription,
		  	  		"method":"createGroup"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					alert("Group Was Created Successfully!");
					$.profile.buildView();
					$('#createGroupForm').dialog('destroy').remove();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	/* *****************************************
	 * 
	 * function to build table to display users 
	 * friend list
	 * 
	 * *****************************************/
	$.profile.buildMyFriends = function(){
		
		//remove unwanted info from view
		$('#welcomeText').remove();
		$('#rightMain').remove();
		$('#leftMain').remove();
		$('#profileDiv').remove();
		$('#groupsRight').remove();
		$('#groupsLeft').remove();
		$('#MyGroupsPage').remove();
		$('#searchResultsPage').remove();
		$('#MyFriends').remove();
		$('#groupSettingsDiv').remove();
		$('#groupProfileUpload').remove();
		$('#connectToFriendsBar').remove();
		$('#settingsView').remove();
		$('#editMembers').remove();
		
		//add logged in users info to nav bar
		$.profile.userActionsRebuildNav();
		
		//call function to gather users friends
		$.publicProfile.GatherFriends();
		
		$('#processing').show();
		
		//set timer to allow data to return from database
		setTimeout(function(){
			
			$('#processing').hide();
			var _friends = $.publicProfile.global.userFriendsList;
			
			//append friends header and beginning of list to view
			$('#welcome')
				.append(
					$('<div/>')
						.attr({'id':'MyFriends'})
						.append(
							$('<h2/>')
								.attr({'id':'groupListHead'})
								.text('Search Results')
						)
						.append(
							$('<ul/>')
								.attr({'id':'friendsList'})
						)
				);
			
			//build list of friends
			$.profile.buildFriendsList(_friends);
			
		},2000);
	};
	
	/* ************************************
	 * 
	 *  function to build friends list
	 * 
	 * ************************************/
	$.profile.buildFriendsList = function(fList){
		
		//loop through users friends and append to friendsList
		for(var i=0;i<fList.length;i++)
		{
			var _curData = fList[i];
			
			$('#friendsList')
				.append(
					$('<li/>')
						.append(
							$('<img/>')
								.attr({'id':'searchedProfilePic','src':'http://www.csehive.com/'+_curData['Profile Picture']})
						)	
						.append(
							$('<label/>')
							.attr('id',_curData['Person ID'])	
							.text("" + _curData['First Name'] + ' ' + _curData['Last Name'])
							.click(function(){$.profile.clickFriend($(this));})
						)
				);
		}
		
		//resize picture to 108x108
		$.profile.cropProfilePic();
		
	};
	
	/* **************************************
	 * 
	 *  function to create action of clicking
	 *  on a friend from the friends list
	 * 
	 * **************************************/
	$.profile.clickFriend = function(link){
		$('#mainBody').PublicProfileView({'PublicPersonId':link.attr('id')});
	};
	
	/* *************************************
	 * 
	 *  function to update the users info
	 *  from settings menu
	 * 
	 * **************************************/
	$.profile.updateSettings = function(_newFName,_newLName,_newEmail){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/UserSettingsDataService",
		  	  	data:{
		  	  		"personId": $.profile.global.PersonId,
		  	  		"firstName":_newFName,
		  	  		"lastName":_newLName,
		  	  		"emailAddress":_newEmail,
		  	  		"method":"updateNoPass"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('userData',html);
					$.profile.global.FirstName = html['userData'][0]['First Name'];
					$.profile.global.LastName = html['userData'][0]['Last Name'];
					$.profile.global.emailAddress = html['userData'][0]['Email Address'];
					$.profile.global.profilePic = 'http://www.csehive.com/'+html['userData'][0]['Profile Picture'];
					alert("Account Updated Successfully!");
					$.profile.buildView();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	/* *****************************************
	 * 
	 *  function to update users info including 
	 *  password from settings menu
	 * 
	 * *****************************************/
	$.profile.updateSettingsWithPassword = function(_newFName,_newLName,_newEmail,_newPass,_newPassCheck){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/UserSettingsDataService",
		  	  	data:{
		  	  		"personId": $.profile.global.PersonId,
		  	  		"firstName":_newFName,
		  	  		"lastName":_newLName,
		  	  		"emailAddress":_newEmail,
		  	  		"password":_newPass,
		  	  		"method":"updateWithPass"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('userData',html);
					$.profile.global.FirstName = html['userData'][0]['First Name'];
					$.profile.global.LastName = html['userData'][0]['Last Name'];
					$.profile.global.emailAddress = html['userData'][0]['Email Address'];
					$.profile.global.profilePic = 'http://www.csehive.com/'+html['userData'][0]['Profile Picture'];
					alert("Account Updated Successfully!");
					$.profile.buildView();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	/* *********************************
	 * 
	 *  function to create profile picture
	 *  upload view
	 * 
	 * *********************************/
	$.profile.uploadProfilePicture = function(){

		var _str = $.profile.global.FirstName+$.profile.global.LastName;
		var _personName = _str.replace(/\s/g, '');
		
		$('#settingsView')
			.append(
				$('<div/>')
					.attr({'id':'fileuploader'})
			);
		
		//build form which will take the allow the user to upload a picture as a profile picture
		$("#fileuploader")
			.append(
				$('<div/>')
					.attr({'id':'userProfileUpload'})
					.append(
						$('<form/>')
							.attr({
								'id':'uploadForm',
								'METHOD':'POST',
								'ENCTYPE':'multipart/form-data',
								'ACTION':'http://www.csehive.com/upload.php',
								'target':'upload__profile_pic_target'
							})
							.append(
								$('<input/>')
									.attr({'id':'file-input', 'name':'myfile', 'type':'file'})
							)
							.append(
								$('<input/>')
									.attr({'id':'personFileName','name':'personFileName'})
									.val(''+_personName+'ProfilePicture.jpg')
									.css({'width':'0px','height':'0px','border':'none'})
							)
							.append(
								$('<input/>')
									.attr({'id':'submitFileButton','type':'submit'})
									.val('Upload')
									.click(function(){
										var _fileExt = $('#file-input').val();
										
										//verify item being uploaded is allowable picture
										if(_fileExt.indexOf(".jpeg") <= 0 &&
												_fileExt.indexOf(".png") <= 0 &&
												_fileExt.indexOf(".bmp") <= 0 &&
												_fileExt.indexOf(".jpg") <= 0)
										{
											alert('Profile Pictures must be JPEG, PNG, or BMP. Please try again.');

											//rebuild view
											$.profile.uploadProfilePicture();
										}
										else
										{
											$.profile.updateUserProfilePicture();
										}
									})
							)
					)
					.append(
						$('<iframe/>')
							.attr({
								'id':'upload__profile_pic_target',
								'name':'upload__profile_pic_target',
								'src':'',
								'style':'width:200px;height:100px;border:0px solid #fff;'
							})
					)
			);
		
		
		$('#fileuploader').dialog({
			'height': '500',
			'width': '600',
			'modal':'false',
			'closeOnEscape': 'true',
            'resizable': 'false',
            'close': function(ev, ui) { 
            	$.profile.buildSettings();
            	$(this).remove();
            }
		});
		
	};
	
	/* *******************************
	 *
	 * function to allow user to upload
	 * a file to their personal repository
	 * 
	 * *******************************/
	$.profile.uploadUserFile = function(_fullUserRepo){
		var _repo = _fullUserRepo+'/';
		
		$('#fileActionDiv')
			.append(
				$('<div/>')
					.attr({'id':'userFileUpload'})
					.append(
							$('<form/>')
							.attr({
								'id':'uploadForm',
								'METHOD':'POST',
								'ENCTYPE':'multipart/form-data',
								'ACTION':'http://www.csehive.com/userupload.php',
								'target':'upload_person_target'
							})
							.append(
								$('<input/>')
									.attr({'id':'file-input', 'name':'myfile', 'type':'file'})
							)
							.append(
								$('<input/>')
									.attr({'id':'personDir','name':'personDir'})
									.val(_repo)
									.css({'width':'0px','height':'0px','border':'none'})
							)
							.append(
								$('<input/>')
									.attr({'id':'submitFileButton','type':'submit'})
									.val('Upload')
							)
					)
					.append(
						$('<iframe/>')
							.attr({
								'id':'upload_person_target',
								'name':'upload_person_target',
								'src':'',
								'style':'width:200px;height:100px;border:0px solid #fff;'
							})
					)	
			);
		
		$('#userFileUpload').dialog({
			'height': '200',
			'width': '600',
			'modal':'false',
			'closeOnEscape': 'true',
            'resizable': 'false',
            'close': function(ev, ui) { 
            	$.profile.buildView();
            	$(this).remove();
            }
		})
		.show();
	};
	
	/* *****************************************
	 * 
	 * update users info for profile picture location
	 * 
	 * *****************************************/
	$.profile.updateUserProfilePicture = function(){
		var _fileName = 'ProfilePictures/'+$('#personFileName').val();
		
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/UserSettingsDataService",
		  	  	data:{
		  	  		"personId": $.profile.global.PersonId,
		  	  		"picturePath":_fileName,
		  	  		"method":"updateProfilePic"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('userData',html);
					$.profile.global.FirstName = html['userData'][0]['First Name'];
					$.profile.global.LastName = html['userData'][0]['Last Name'];
					$.profile.global.emailAddress = html['userData'][0]['Email Address'];
					$.profile.global.profilePic = 'http://www.csehive.com/'+html['userData'][0]['Profile Picture'];
					alert("Account Updated Successfully!");
					$('#fileuploader').dialog('destroy').remove();
					$.profile.buildView();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	/* ********************************************
	 * 
	 *  function which calls jquery plugin to 
	 *  resize image to defined size
	 * 
	 * ********************************************/
	$.profile.cropProfilePic = function(){
		 $('#profilePicture').resizecrop({
				'width': '108',
				'height': '108',
				'vertical': "top"
			});
		 
		 $('#groupPicture').resizecrop({
				'width': '108',
				'height': '108',
				'vertical': "top"
			});
		 
		 $('.searchProfilePicture').resizecrop({
				'width': '108',
				'height': '108',
				'vertical': "top"
			});		 
	 };
	 
	 /* ************************************
	  * 
	  *  function to replace nac bar info 
	  *  with info from logged in user
	  * 
	  * ************************************/
	 $.profile.userActionsRebuildNav = function(){
		$('#profilePicture').remove();
		$('#username').remove();
		$('#groupPicture').remove();
		 
		 $('#nav')
			/*.append(
				$('<img/>')
					.attr({'id':'profilePicture','src':$.profile.global.profilePic})
			)*/
			.append(
				$('<div/>')
					.attr({'id':'username'})
					.append(
						$('<label/>')
							.attr({'id':'UsersName'})
							.text($.profile.global.FirstName+" "+$.profile.global.LastName)
					)
			);
		
		$.profile.cropProfilePic();
		 
	 };
	
})(jQuery);