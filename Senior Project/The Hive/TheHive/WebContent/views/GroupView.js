(function($) {
	var methods = {
			"init": function(options){
				$.extend( $.group.global, options );
				return this.each(function(){
					$.group.create();
				});
			}
		};

	/* *****************************
	 * 
	 * create namespace
	 *
	 ******************************/
	$.group = {};
	
	/* *********************************
	 * 
	 *  create global variables
	 *
	 * *********************************/
	$.group.global = {
		"groupId": null,
		"groupDetails": null,
		"groupList": null,
		"groupMembers": null,
		"groupRepoLocation": null,
		"currentRequest": null,
		
	};
	
	/* ************************************
	 * 
	 *  declare method to build view from jsp
	 * 
	 * ************************************/
	$.fn.GroupView = function( method ) {	    
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
	$.group.destroy = function(){
		if($.group.global.currentRequest!==null)
		{
			$.group.global.currentRequest.abort();
		}
		
		if($.group.global.container !== null)
		{
			$.group.global.container.remove();
		}
		
		$('#mainBody').ProfileView("destroy");
	};
	
	/* **********************************
	 * 
	 *  create create function
	 * 
	 * **********************************/
	$.group.create = function()
	{
		$.group.global.containingDiv = $('<div/>').attr('id','group');
		
		$.group.buildView();
	};
	
	/* ************************************
	 * 			BUILD VIEW  
	 * ************************************/
	$.group.buildView = function()
	{	
		$.group.destroyProfileScreen();
		
		$.group.gatherGroupInfo();
		
		$('#processing').show();
		
		//set a timeout to allow for the JSON to return the group data for the page
		setTimeout(function()
		{
			$('#groupFileUpload').dialog('destroy').remove();
			$.group.gatherGroupMembers();
			$('#processing').hide();
			
			var _linkDiv = $('#links');
			var _groupNav = $('#nav');
			
			
			
			_groupNav
			.append(
				$('<img/>')
				.attr({'id':'groupPicture','src':'http://www.csehive.com/'+$.group.global.groupDetails[0]['Profile Picture']})
				)
			.append(
				$('<div/>')
					.attr({'id':'username'})
					.append(
						$('<label/>')
							.attr({'id':'GroupName'})
							//.text('Capstone')
							.text($.group.global.groupDetails[0]['Group Name'])
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
								$.profile.buildSearchResults();
							})
					)
				);
			
			var _groupBody = $('#welcome');
			
			_groupBody
			.append(
				$('<div/>')
				.attr({'id':'groupsLeft'})
					.append(
						$('<div/>')
						.attr({'id':'groupLinks'})
							.append(
								$('<h2/>')
								.attr({'id':'linksTitle'})
									.text('Group Links')
								)
							.append(
								$('<ul/>')
									.append(
										$('<li/>')
											.append(
												$('<label/>')
													.attr({'id':'groupLink'})
													.text('Group Information')
													.click(function(){
														$.group.buildView();
														})
													)
											)
									.append(
										$('<li/>')
											.append(
												$('<label/>')
													.attr({'id':'discussionLink'})
													.text('Discussions')
													.click(function(){
														$.group.destroyForDiscussion();
														$('#mainBody').DiscussionView({'groupId':$.group.global.groupId});
														})
													)
											)
									.append(
										$('<li/>')
											.append(
												$('<label/>')
													.text('Repository')
													.click(function(){
														$.group.destroyForRepo();
														$.group.showRepo();
													})
												)
										)
									)
							)
						.append(
								$('<div/>')
								.attr({'id':'memberList'})
									.append(
										$('<h2/>')
										.attr({'id':'memberTitle'})
											.text('Member List')
										)
								)
					)
				.append(
					$('<div/>')
					.attr({'id':'groupsRight'})
						.append(
									$('<div/>')
										.attr({'id':'adminSettings'})
						)
						.append(
							$('<div/>')
							.attr({'id':'groupData'})
								.append(
									$('<div/>')
									.attr({'id':'groupHeader'})
										.append(
											$('<h3/>')
												.text('Welcome to '+$.group.global.groupDetails[0]['Group Name'])
											)
									)
								.append(
									$('<div/>')
									.attr({'id':'groupInfo'})
										.append(
											$('<p/>')
												.append(
													$('<label/>')
														.text('Course Title: '+$.group.global.groupDetails[0]['Class Title'])
												)
										)
										.append(
											$('<p/>')
												.text('About the group: '+$.group.global.groupDetails[0]['Group Description'])
										)
										.append(
											$('<p/>')
												.text('Group Administrator: '+$.group.global.groupDetails[0]['Person ID'])
										)
									)
							)
					);
			
			$.group.checkIfAdmin();
			
			$.profile.cropProfilePic();
			
		}, 1000);
		
		
	};

	$.group.checkIfAdmin = function(){
		var _signedInUser = $.profile.global.FirstName+" "+$.profile.global.LastName;
		var _groupAdmin = $.group.global.groupDetails[0]['Person ID'];
		
		if(_signedInUser == _groupAdmin)
		{
			$('#adminSettings')
				.append(
					$('<img/>')
						.attr({'id':'groupAdminIcon','src':'images/group_settings.png'})
						.click(function(){
							$.group.buildSettingsView();
						})
				);
			
		}
	};
	
	
	$.group.destroyProfileScreen = function(){
		$('#profileDiv').remove();
		$('#username').remove();
		$('#profilePicture').remove();
		$('#welcomeText').remove();
		$('#rightMain').remove();
		$('#leftMain').remove();
		$('#navBar').remove();
		$('#searchBar').remove();
		$('#discussionList').remove();
		$('#groupsLeft').remove();
		$('#groupsRight').remove();
		$('#groupPicture').remove();
		$('#searchResultsPage').remove();
		$('#MyGroupsPage').remove();
		$('#userProfileUpload').remove();
		$('#groupSettingsDiv').remove();
		$('#MyFriends').remove();
		$('#settingsList').remove();
		$('#commentBar').remove();
		$('#commentsDiv').remove();
		$('#connectToFriendsBar').remove();
		$('#adminSettings').remove();
		$('#editMembers').remove();
		$('#groupProfileUpload').remove();
		$('#settingsView').remove();
	};
	
	$.group.destroyForRepo = function(){
		$('#groupData').remove();
		$('#adminSettings').remove();
		$('#discussionList').remove();
		$('#userProfileUpload').remove();
		$('#groupSettingsDiv').remove();
		$('#groupFiles').remove();
		$('#settingsView').remove();
		$('#groupProfileUpload').remove();
		$('#editMembers').remove();
	};
	
	$.group.destroyForDiscussion = function(){
		$('#groupData').remove();
		$('#settingsView').remove();
		$('#discussionList').remove();
		$('#editMembers').remove();
		$('#groupFiles').remove();
		$('#userProfileUpload').remove();
		$('#adminSettings').remove();
		$('#groupProfileUpload').remove();
		$('#groupSettingsDiv').remove();
	};
	
	
	$.group.buildSettingsView = function(){
		$('#groupData').remove();
		$('#settingsView').remove();
		$('#adminSettings').remove();
		$('#editMembers').remove();
		$('#groupProfileUpload').remove();
		
		$('#groupsRight')
			.append(
				$('<div/>')
					.attr({'id':'groupSettingsDiv'})
					.append(
						$('<label/>')
							.text('Click here to manage group members')
							.click(function(){
								$.group.manageGroupMembers();
							})
					)
					.append($('<br/>'))
					.append(
						$('<label/>')
							.attr('id','updatePicLink')
							.text('Click here to update groups profile picture.')
							.click(function(){
								$.group.uploadProfilePicture();
							})
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
								.text('Group Name:')
							)
					)
				.append(
					$('<li/>')
						.append(
							$('<label/>')
								.text('Course Name:')
							)
					)
				.append(
					$('<li/>')
						.append(
							$('<label/>')
								.text('Group Description:')
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
								.attr({'id':'groupNameSetting','type':'text','readonly':'readonly'})
								.val($.group.global.groupDetails[0]['Group Name'])
								.dblclick(function(){
									$(this).removeAttr('readonly');
									$(this).css({'color':'#000000'});
								})
							)
					)
				.append(
					$('<li/>')
						.append(
							$('<input/>')
								.attr({'id':'courseNameSetting','type':'text','readonly':'readonly'})
								.val($.group.global.groupDetails[0]['Class Title'])
								.dblclick(function(){
									$(this).removeAttr('readonly');
									$(this).css({'color':'#000000'});
								})
							)
					)
				.append(
					$('<li/>')
						.append(
							$('<textarea/>')
								.attr({'id':'groupDescrSetting','rows':'6','cols':'30','readonly':'readonly'})
								.val($.group.global.groupDetails[0]['Group Description'])
								.dblclick(function(){
									$(this).removeAttr('readonly');
									$(this).css({'color':'#000000'});
								})
							)
				)
				.append(
					$('<li/>')
						.append(
							$('<button/>')
								.text('Save Changes')
								.click(function(){
									var _groupName = $('#groupNameSetting').val();
									var _groupDesc = $('#groupDescrSetting').val();
									var _courseName = $('#courseNameSetting').val();
									var _groupId = $.group.global.groupDetails[0]['Group ID'];
									
									$.group.updateGroupSettings(_groupName,_groupDesc,_courseName,_groupId);
								})
						)
				)
		)
)
			);
			
	};
	
	$.group.manageGroupMembers = function(){
		
		var _members = $.group.global.groupMembers; 
		
		$('#groupData').remove();
		$('#adminSettings').remove();
		$('#groupSettingsList').remove();
		$('#editMembers').remove();
		$('#settingsView').remove();
		$('#editMembers').remove();
		$('#groupProfileUpload').remove();
		$('#leftSettings').remove();
		$('#rightSettings').remove();
		$('#updatePicLink').remove();
		
		$('#groupsRight')
			.append(
				$('<div/>')
					.attr({'id':'editMembers'})
					.append(
						$('<ul/>')
							.attr({'id':'editMembersList'})
					)
			);
		
		for(var i=0;i<_members.length;i++)
		{
			_curData = _members[i];
			
			$('#editMembersList')
				.append(
					$('<li/>')
						.attr({'id':_curData['Person ID']})
						.append(
							$('<span/>')
								.attr({'id':'personName'})
								.append(
									$('<label/>')
										.text(_curData['PErson Name'])
								)
						)
						.append(
							$('<span/>')
								.attr({'id':'removeMemberLink'})
								.append(
									$('<label/>')
										.text('       Remove Member')
										.css({'color':'blue'})
								)
						)
						.click(function(){
							$.group.removeUserFromGroup($(this));
						})
				);
		}
	};
	
	$.group.removeUserFromGroup = function(link){
		
		var _loggedInUser = $.profile.global.PersonId;
		
		if(link.attr('id') == _loggedInUser)
		{
			alert('Member is group admin and cannot be removed. Please try again');
		}
		else
		{
			$.profile.global.currentRequest = 
				$.ajax({ 
			  	  	url:"/TheHive/GroupDataService",
			  	  	data:{
			  	  		"groupId": $.group.global.groupDetails[0]['Group ID'],
				  	  	"personId": link.attr('id'),
			  	  		"method":"removeMember"
			  	  	},
					type:'POST',
					dataType:"json",
					success: function(html){
						$('#globalSettings').data('GroupDetails',html);
						$.group.global.groupDetails = html['GroupDetails'];
						alert('Member removed successfully.');
						$.group.buildView();
					},
					error: function(html){
						alert("An error occured while gathering profile information." +
								"\nPlease refresh your browser and try again.");
					}
				}); //Closes ajax
		}
	};
	
	
	$.group.updateGroupSettings = function(_groupName,_groupDesc,_courseName,_groupId){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/GroupDataService",
		  	  	data:{
		  	  		"groupId": _groupId,
			  	  	"groupName": _groupName,
				  	"groupDescr": _groupDesc,
				  	"courseName": _courseName,
		  	  		"method":"updateGroup"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('GroupDetails',html);
					$.group.global.groupDetails = html['GroupDetails'];
					$.group.buildView();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	
	$.group.showRepo = function(){
		$('#groupData').remove();
		$('#adminSettings').remove();
		$('#adminSettings').remove();
		$('#groupSettingsDiv').remove();
		$('#userProfileUpload').remove();
		$('#editMembers').remove();
		$('#groupProfileUpload').remove();
		$('#settingsView').remove();
		$('#discussionList').remove();
		$('#commentBar').remove();
		$('#commentsDiv').remove();
		
		$('#groupsRight')
			.append(
				$('<div/>')
					.attr({'id':'groupFiles'})
					.append(
						$('<div/>')
							.attr({'id':'fileActionDiv'})
							.append(
								$('<table/>')
									.attr('id','fileRepoAction')
									.append(
										$('<tr/>')
											.append(
												$('<td/>')
													.attr({'id':'uploadFileLink'})
													.append(
														$('<label/>')
															.text('Upload')
															.click(function(){
																var _fullGroupRepo = '/var/www/GroupRepo/'+$.group.global.groupDetails[0]['Repository Root'];
																$.group.uploadGroupFile(_fullGroupRepo);
															})
													)
											)
											/*.append(
												$('<td/>')
													.attr({'id':'uploadFileLink'})
													.append(
														$('<label/>')
															.text('Delete')
															.click(function(){
																alert('Delete File Link Clicked');
															})
													)
											)*/
									)
							)
					)
					.append($('<div/>').attr('id','fileExplorer'))
			);
		
		$('#fileExplorer').fileTree({
			root:'/var/www/GroupRepo/'+$.group.global.groupDetails[0]['Repository Root'],
			script:'plugins/jqueryFileTree.jsp'},
			function(file){
				e.preventDefault();  
				
				var _stringToRemove = '/var/www/';
				var _fileloc = file.replace(_stringToRemove,'');
				
				window.location.hre = _fileloc;
			});
		
	};
	
	$.group.gatherGroupInfo = function(){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/GroupDataService",
		  	  	data:{
		  	  		"groupId": $.group.global.groupId,
		  	  		"method":"getDetails"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('GroupDetails',html);
					$.group.global.groupDetails = html['GroupDetails'];
					//alert("it worked");
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		return false;
	};
	
	$.group.gatherGroupMembers = function(){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/GroupDataService",
		  	  	data:{
		  	  		"groupId": $.group.global.groupId,
		  	  		"method":"getMembers"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('GroupMembers',html);
					$.group.global.groupMembers = html['GroupMembers'];
					$.group.buildGroupList();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		return false;
	};
	
	$.group.buildGroupList = function(){
		
		var _members = $.group.global.groupMembers;

		$('#memberList')
			.append(
				$('<ul/>')
					.attr('id','membersLinks')
			);
		
		for(var i=0;i<_members.length;i++)
		{
			var _curData = _members[i];
			
			$('#membersLinks')
				.append(
					$('<li/>')
						.append(
							$('<label/>')
							.attr('id',_curData['Person ID'])	
							.text(_curData['PErson Name'])
							.click(function(){$.group.clickMember($(this));})
								
						)
				);
		}
		
		$.group.checkIfMember();
		
	};
	
	$.group.checkIfMember = function(){
		var _members = $.group.global.groupMembers;
		var _curUser = $.profile.global.PersonId;
		var _isMember = false;
		
		for( var i=0;i<_members.length;i++)
		{
			var _curData = _members[i];
			
			if(_curData['Person ID'] === _curUser)
			{
				_isMember = true;
			}
		}
		
		if(!_isMember)
		{
			$('#memberList')
			.append(
				$('<label/>')
					.text('Click here to join this group')
					.click(function(){
						$.group.addNewMember();
					})
			);
		}
		
	};
	
	$.group.addNewMember = function(){
		
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/GroupDataService",
		  	  	data:{
		  	  		"groupId": $.group.global.groupDetails[0]['Group ID'],
		  	  		"groupName": $.group.global.groupDetails[0]['Group Name'],
			  	  	"personId": $.profile.global.PersonId,
		  	  		"method":"addNewMember"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('GroupMembers',html);
					$.group.global.groupMembers = html['GroupMembers'];
					$.group.buildView();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		
	};
	
	
	$.group.clickMember = function(link){
		$('#mainBody').PublicProfileView({'PublicPersonId':link.attr('id')});
	};
	
	$.group.uploadProfilePicture = function(){
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
		$('#connectToFriendsBar').remove();
		//$('#settingsView').remove();
		$('#userProfileUpload').remove();
		$('#editMembers').remove();
		$('#groupProfileUpload').remove();
		
		var _str = $.group.global.groupDetails[0]['Group Name'];
		var _personName = _str.replace(/\s/g, '');
		
		$('#welcome')
		.append(
			$('<div/>')
				.attr({'id':'fileuploader'})
		);
	
	$("#fileuploader")
		.append(
			$('<div/>')
				.attr({'id':'groupProfileUpload'})
				.append(
					$('<form/>')
						.attr({
							'id':'uploadForm',
							'METHOD':'POST',
							'ENCTYPE':'multipart/form-data',
							'ACTION':'http://www.csehive.com/upload.php',
							'target':'upload_target'
						})
						.append(
							$('<input/>')
								.attr({'id':'file-input', 'name':'myfile', 'type':'file'})
						)
						.append(
							$('<input/>')
								.attr({'id':'groupFileName','name':'personFileName'})
								.val(_personName+'ProfilePicture.jpg')
								.css({'width':'0px','height':'0px','border':'none'})
						)
						.append(
							$('<input/>')
								.attr({'id':'submitFileButton','type':'submit'})
								.val('Upload')
								.click(function(){
									var _fileExt = $('#file-input').val();
									if(_fileExt.indexOf(".jpeg") <= 0 &&
											_fileExt.indexOf(".png") <= 0 &&
											_fileExt.indexOf(".bmp") <= 0 &&
											_fileExt.indexOf(".jpg") <= 0)
									{
										alert('Profile Pictures must be JPEG, PNG, or BMP. Please try again.');
										$.group.uploadProfilePicture();
									}
									else
									{
										$.group.updateGroupProfilePicture();
									}
								})
						)
				)
				.append(
					$('<iframe/>')
						.attr({
							'id':'upload_target',
							'name':'upload_target',
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
        	$.group.buildSettings();
        	$(this).remove();
        }
	});
		
	};
	
	$.group.updateGroupProfilePicture = function(){
		var _fileName = 'ProfilePictures/'+$('#groupFileName').val();
		
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/GroupDataService",
		  	  	data:{
		  	  		"groupId": $.group.global.groupDetails[0]['Group ID'],
			  	  	"filePath": _fileName,
		  	  		"method":"updateGroupPicture"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('GroupDetails',html);
					$.group.global.groupDetails = html['GroupDetails'];
					//alert('Member removed successfully.');
					$('#fileuploader').dialog('destroy').remove();
					$.group.buildView();
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	$.group.uploadGroupFile = function(_fullRepoPath){
		
		var _repo = _fullRepoPath+'/';
		
		$('#fileActionDiv')
			.append(
				$('<div/>')
					.attr({'id':'groupFileUpload'})
					.append(
							$('<form/>')
							.attr({
								'id':'uploadForm',
								'METHOD':'POST',
								'ENCTYPE':'multipart/form-data',
								'ACTION':'http://www.csehive.com/groupupload.php',
								'target':'upload_group_target'
							})
							.append(
								$('<input/>')
									.attr({'id':'file-input', 'name':'myfile', 'type':'file'})
							)
							.append(
								$('<input/>')
									.attr({'id':'groupDir','name':'groupDir'})
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
								'id':'upload_group_target',
								'name':'upload_group_target',
								'src':'',
								'style':'width:200px;height:100px;border:0px solid #fff;'
							})
					)	
			);
	
	$('#groupFileUpload').dialog({
		'height': '200',
		'width': '600',
		'modal':'false',
		'closeOnEscape': 'true',
        'resizable': 'false',
        'close': function(ev, ui) { 
        	$.group.buildView();
        	$(this).remove();
        }
	})
	.show();
		
	};
	
	
})(jQuery);