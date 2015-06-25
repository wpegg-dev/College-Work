(function($) {
	var methods = {
			"init": function(options){
				$.extend( $.publicProfile.global, options );
				return this.each(function(){
					$.publicProfile.create();
				});
			}
		};

	/* *****************************
	 * 
	 * create namespace
	 *
	 ******************************/
	$.publicProfile = {};
	
	/* *********************************
	 * 
	 *  create global variables
	 *
	 * *********************************/
	$.publicProfile.global = {
		"emailAddress": null,
		"FirstName": null,
		"LastName": null,
		"PublicPersonId": null,
		"currentRequest": null
		
	};
	
	/* ************************************
	 * 
	 *  declare method to build view from jsp
	 * 
	 * ************************************/
	$.fn.PublicProfileView = function( method ) {	    
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
	$.publicProfile.destroy = function(){
		if($.publicProfile.global.currentRequest!==null)
		{
			$.publicProfile.global.currentRequest.abort();
		}
		
		if($.publicProfile.global.container !== null)
		{
			$.publicProfile.global.container.remove();
		}
		
		$('#mainBody').PublicProfileView("destroy");
	};
	
	/* **********************************
	 * 
	 *  create create function
	 * 
	 * **********************************/
	$.publicProfile.create = function()
	{
		$.publicProfile.global.containingDiv = $('<div/>').attr('id','userProfile');
		
		$.publicProfile.buildView();
	};
	
	/* ************************************
	 * 			BUILD VIEW  
	 * ************************************/
	$.publicProfile.buildView = function()
	{	
		$.publicProfile.destroyLoginScreen();
		$.publicProfile.getPersonData();
		$.publicProfile.buildFriendsBar();
		
		
		$('#processing').show();
		setTimeout(function()
		{
			$.publicProfile.loadGroups($.publicProfile.global.publicProfileData);
			$.publicProfile.gatherGroups();
			//$.publicProfile.GatherFriends();
			$.publicProfile.buildFriendsBar();
			$('#processing').hide();
				var _linkDiv = $('#links');
				var _navBar = $('#nav');
				
				_navBar
					.append(
						$('<img/>')
							.attr({'id':'profilePicture','src':'http://csehive.com/'+$.publicProfile.global.publicProfileData[0]['Profile Picture']})
					)
					.append(
						$('<div/>')
							.attr({'id':'username'})
							.append(
								$('<label/>')
									.attr({'id':'UsersName'})
									.text($.publicProfile.global.publicProfileData[0]['First Name']+" "+$.publicProfile.global.publicProfileData[0]['Last Name'])
							)
							
					);
				
				
				
				var _groupsDiv = 
						$('<div/>')
							.attr({'id':'groups'});
				
				
				var _containingDiv = $('#welcome');
				
				_containingDiv
					.append(
						$('<div/>')
							.attr({'id':'profileDiv'})
							.append(
								$.publicProfile.global.friendBar
							)
							.append(
								_groupsDiv		
							)
							.append(
								$('<div/>')
									.attr({'id':'filesDiv'})
									.append(
										$('<p/>')
											.append(
												$('<label/>')
													.text('First Name: '+$.publicProfile.global.publicProfileData[0]['First Name'])
											)
									)
									.append(
										$('<p/>')
											.append(
												$('<label/>')
													.text('Last Name: '+$.publicProfile.global.publicProfileData[0]['Last Name'])
											)
									)
									.append(
										$('<p/>')
											.append(
												$('<label/>')
													.text('Contact Information: '+$.publicProfile.global.publicProfileData[0]['Email Address'])
											)
									)
							)
				);
				
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
													$.profile.buildSettings();
													})
												)
										)
								)
						);
				
				_linkDiv
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
											$.publicProfile.searchAction();
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
										$.publicProfile.buildSearchResults();
									})
							)
					);
				
				$.profile.cropProfilePic();
				
		},3000);
	};
	
	/* ***************************************
	 * 
	 *  function to gather the persons profile data
	 * 
	 * ***************************************/
	$.publicProfile.getPersonData = function(){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/UserLoginService",
		  	  	data:{
		  	  		"personId": $.publicProfile.global.PublicPersonId,
		  	  		"method":"getPublic"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('publicProfileData',html);
					//$.publicProfile.loadGroups(html['publicProfileData']);
					$.publicProfile.global.publicProfileData = html['publicProfileData'];
					//alert("it worked");
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	
	/* *************************************************
	 * 
	 *  function to set the action of pressing ENTER
	 *  in the search field to the .click function of the 
	 *  search icon
	 * 
	 * *************************************************/
	$.publicProfile.searchAction = function(){
		$("#searchSubmit").click();
	};
	
	
	/* **********************************************
	 * 
	 *  function to load groups onto the page
	 * 
	 * **********************************************/
	$.publicProfile.loadGroups = function(groupData){
		
		var _groupDiv = $('#groups');
		var _displayData = groupData;
		
		_groupDiv
			.append(
				$('<h2/>')
					.text('Groups')
					.attr('id','groupListHead')
			)
			.append(
				$('<ul/>')
					.attr('id','groupList')
			);
		
		for(var i=0;i<_displayData.length;i++)
		{
			var _curData = _displayData[i];
			//var _id = _curData['Group ID'];
			
			$('#groupList')
				.append
				(
					$('<li/>')
						.append(
							$('<label/>')
								.attr({'id':_curData['Group ID']})
								.addClass('groupsLink')
								.text(_curData['Group Name'])
								.click(function(){$.publicProfile.clickGroup($(this));})
						)
				); 
		}		
	};
	
	/* *****************************************
	 * 
	 *  set click event on each group name to 
	 *  build the group profile
	 * 
	 * *****************************************/
	$.publicProfile.clickGroup = function(link){
		$('#mainBody').GroupView({'groupId':link.attr('id')});
	};
	
	
	/* ******************************************
	 * 
	 *  clear the page of all contents
	 * 
	 * ******************************************/
	$.publicProfile.destroyLoginScreen = function(){
		$('#welcomeText').remove();
		$('#rightMain').remove();
		$('#leftMain').remove();
		$('#profileDiv').remove();
		$('#searchBar').remove();
		$('#groupSettingsDiv').remove();
		$('#username').remove();
		$('#profilePicture').remove();
		$('#groupsRight').remove();
		$('#groupsLeft').remove();
		$('#adminSettings').remove();
		$('#navBar').remove();
		$('#groupProfileUpload').remove();
		$('#groupPicture').remove();
		$('#MyGroupsPage').remove();
		$('#editMembers').remove();
		$('#settingsView').remove();
		$('#userProfileUpload').remove();
		$('#searchResultsPage').remove();
		$('#friendBar').remove();
		$('#MyFriends').remove();
		$('#settingsList').remove();
	};
	
	/* ***********************************
	 * 
	 * function to get the simple group info for profile page
	 * 
	 * ***********************************/
	$.publicProfile.gatherGroups = function(){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/UserLoginService",
		  	  	data:{
		  	  		"personId": $.publicProfile.global.publicProfileData[0]['Person ID'],
		  	  		"method":"getGroups"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('simpleGroupData',html);
					$.publicProfile.loadGroups(html['simpleGroupData']);
					$.publicProfile.global.simpleGroups = html['simpleGroupData'];
					//alert("it worked");
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
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
	$.publicProfile.buildMyGroups = function(){
		
		$.publicProfile.destroyLoginScreen();
		$.publicProfile.getPersonData();
		
		var _displayData = $.publicProfile.global.simpleGroups;
		
		var _navBar = $('#nav');
		
		_navBar
			.append(
				$('<img/>')
					.attr({'id':'profilePicture','src':'http://csehive.com/'+$.publicProfile.global.publicProfileData[0]['Profile Picture']})
			)
			.append(
				$('<div/>')
					.attr({'id':'username'})
					.append(
						$('<label/>')
							.attr({'id':'UsersName'})
							.text($.publicProfile.global.publicProfileData[0]['First Name']+" "+$.publicProfile.global.publicProfileData[0]['Last Name'])
					)
					
			)
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
								.attr({'id':'searchText', 'type':'text'})
								.keyup(function(event){
									
									if(event.keyCode === 13){
										event.preventDefault();
										event.stopPropagation();
										$.publicProfile.searchAction();
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
									$.publicProfile.buildSearchResults();
								})
						)
				);
		
		
		$('#welcome')
			.append(
				$('<div/>')
					.attr({'id':'MyGroupsPage'})
			);
		
		var _groupDiv =  $('#MyGroupsPage');
			
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
		
		$('#groupList')
			.append(
				$('<label/>')
					.attr({'id':'final'})
					.text('Create Group')
					.click(function(){
						alert('Build dialog box for creating a new group');
						})
				)
			.append(
				$('<br>')	
				);
		
			for(var i=0;i<_displayData.length;i++)
			{
				var _curData = _displayData[i];
				//var _id = _curData['Group ID'];
				
				$('#groupList')
					.append
					(
						$('<li/>')
							.append(
								$('<label/>')
									.attr({'id':_curData['Group ID']})
									.addClass('groupsLink')
									.text(_curData['Group Name'])
									.click(function(){$.publicProfile.clickGroup($(this));})
							)
					); 
			}
			
			$.profile.cropProfilePic();
			
	};
	
	/* ***************************************
	 * 
	 *  function to build settings view
	 * 
	 * ***************************************/
	$.publicProfile.buildSettings = function(){
		alert('Load Settings View Here');
	};
	
	/* *************************************
	 * 
	 *  function to create search results
	 * 
	 * *************************************/
	$.publicProfile.buildSearchResults = function(){
		
		$.profile.userActionsRebuildNav();
		
		$.publicProfile.getSearchResults($('#search').val());
		
		$('#processing').show();
		setTimeout(function()
		{
			$('#processing').hide();
			$('#profileDiv').remove();
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
		
			var _displayData = $.publicProfile.global.searchResults;
			
			for(var i=0;i<_displayData.length;i++)
			{
				var _curData = _displayData[i];
				//var _id = _curData['Group ID'];
				
				$('#searchResultList')
					.append
					(
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
									.click(function(){$.publicProfile.clickSearch($(this));})
							)
					); 
			}
			
			$.profile.cropProfilePic();
			
		},2000);
		
	};
	
	$.publicProfile.clickSearch = function(link){
		
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
	$.publicProfile.getSearchResults = function(keyword){
		
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
					$.publicProfile.global.searchResults = html['searchResults'];
					//alert("it worked");
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		//return false;
		
	};
	
	/* ******************************************************
	 * 
	 *  function to check if user if on their own profile 
	 *  and if not, build a bar which asks if the user would
	 *  like to add the person as a friend
	 * 
	 * ******************************************************/
	$.publicProfile.buildFriendsBar = function(){
		
		$.publicProfile.GatherFriends();
		$('#connectToFriendsBar').remove();
		$('#editMembers').remove();
		$('#settingsView').remove();
		$('#groupProfileUpload').remove();
		
		$('#processing').show();
		setTimeout(function()
		{
			$('#processing').hide();
			$.publicProfile.global.friendBar =
				$('<div/>')
					.attr({'id':'connectToFriendsBar'});
			
			var _friendList = $.publicProfile.global.userFriendsList;
			var _isFriend = false;
			
			for(var i=0;i<_friendList.length;i++)
			{
				var _curData = _friendList[i];
				
				if((_curData['Person ID'] == $.publicProfile.global.PublicPersonId) ||
						($.profile.global.PersonId == $.publicProfile.global.PublicPersonId))
				{
					_isFriend = true;
				}
				
			}
			
			if(!_isFriend)
			{
				$('#connectToFriendsBar')
					.append(
						$('<div/>')
							.attr({'id':'friendBar'})
							.append(
										$('<label/>')
											.text('Would you like to add this person as a friend?')
									)
									.append(
										$('<button/>')
											.attr({'id':'final'})
											.text('Add Friend!')
											.css({'font-size':'13px','width':'130px'})
											.click(function(){
												$.publicProfile.addFriend();
											})
									)
					);
			}
		},1000);
	};
	
	
	/* ***************************************************************
	 * 
	 * function to add a person as a friend
	 * 
	 * ***************************************************************/
	$.publicProfile.addFriend = function(){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/FriendDataService",
		  	  	data:{
		  	  		"personId": $.profile.global.PersonId,
		  	  		"friendId": $.publicProfile.global.PublicPersonId,
		  	  		"method":"addFriend"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					alert("You Are Now Friends!");
					$.publicProfile.buildView();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	/* ****************************************************************
	 * 
	 * this will get the list of friends that the logged in user has
	 * 
	 * 
	 * ****************************************************************/
	$.publicProfile.GatherFriends = function(){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/FriendDataService",
		  	  	data:{
		  	  		"personId": $.profile.global.PersonId,
		  	  		"method":"checkFriends"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('friendsList',html);
					$.publicProfile.global.userFriendsList = html['friendsList'];
					//alert("it worked");
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		
	};
	
	
	
	
	
	
})(jQuery);