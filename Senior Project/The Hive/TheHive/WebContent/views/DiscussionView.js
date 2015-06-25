(function($) {
	var methods = {
			"init": function(options){
				$.extend( $.discussion.global, options );
				return this.each(function(){
					$.discussion.create();
				});
			}
		};

	/* *****************************
	 * 
	 * create namespace
	 *
	 ******************************/
	$.discussion = {};
	
	/* *********************************
	 * 
	 *  create global variables
	 *
	 * *********************************/
	$.discussion.global = {
		"groupId": null,
		"discussionList": null,
		"commentList": null,
		"currentRequest": null,
		
	};
	
	/* ************************************
	 * 
	 *  declare method to build view from jsp
	 * 
	 * ************************************/
	$.fn.DiscussionView = function( method ) {	    
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
	$.discussion.destroy = function(){
		if($.discussion.global.currentRequest!==null)
		{
			$.discussion.global.currentRequest.abort();
		}
		
		if($.discussion.global.container !== null)
		{
			$.discussion.global.container.remove();
		}
		
		$('#mainBody').ProfileView("destroy");
	};
	
	/* **********************************
	 * 
	 *  create create function
	 * 
	 * **********************************/
	$.discussion.create = function()
	{
		$.discussion.global.containingDiv = $('<div/>').attr('id','userProfile');
		
		$.discussion.buildView();
	};
	
	/* ************************************
	 * 			BUILD VIEW  
	 * ************************************/
	$.discussion.buildView = function()
	{	
		$.discussion.destroyLoginScreen();
		$.discussion.gatherDiscussions();

		$('#processing').show();
		setTimeout(function(){
			
			$('#processing').hide();
	
			var _groupBody = $('#groupsRight');
	
			_groupBody
				.append(
					$('<div/>')
						.attr({'id':'discussionList'})
						.append(
							$('<div/>')
								.attr({'id':'discussionBar'})
								.append(
									$('<label/>')
										.attr({'id':'final'})
										.text('Create New')
										.click(function(){
											$.discussion.createDiscussion();
										})
								)
						)
				);
					
				var _dTopics = $.discussion.global.topics;
				
				for(var i=0;i<_dTopics.length;i++)
				{
					var _curData = _dTopics[i];
					
					$('#discussionList')
						.append(
							$('<div/>')
								.attr({'id':''+_curData['Discussion ID']})
								.append(
									$('<div/>')
										.attr({'id':'topicTitle'})
										.append(
											$('<span/>')
												.attr({'id':'date'})
												.text(_curData['Create Date'])
										)
										.append(
											$('<span/>')
												.attr({'id':'title'})
												.text(_curData['Discussion Text'])
										)
										.append(
											$('<span/>')
												.attr({'id':'Author'})
												.text('Created By: '+ _curData['Person Name'])
										)
								)
								.dblclick(function(){
									$.discussion.buildCommentView($(this));
								})
						);
				};
				
				$('#discussionList')
					.append(
						$('<label/>')
							.attr({'id':'note'})
							.text('*Double-Click on a topic to view comments.')
					);
				
				
		},2000);
	};
	
	$.discussion.destroyLoginScreen = function(){
		$('#welcomeText').remove();
		$('#rightMain').remove();
		$('#leftMain').remove();
		$('#profileDiv').remove();
		$('#profilePicture').remove();
		$('#groupData').remove();
		$('#editMembers').remove();
		$('#groupSettingsDiv').remove();
		$('#userProfileUpload').remove();
		$('#connectToFriendsBar').remove();
		$('#commentBar').remove();
		$('#profileDiv').remove();
		$('#commentsDiv').remove();
		$('#searchResultsPage').remove();
		$('#discussionList').remove();
		$('#groupProfileUpload').remove();
		$('#adminSettings').remove();
		$('#settingsView').remove();
	};
	
	$.discussion.createDiscussion = function(){
		
		$('#groupsRight')
			.append(
				$('<div/>')
					.attr({'id':'createDiscussionForm'})
			);
		
		var _form = $('#createDiscussionForm');
		
		_form.hide();
		
		_form
			.append(
				$('<p/>')
					.append(
						$('<label/>')
							.text('Discussion Topic: ')
					)
					.append(
						$('<input/>')
							.attr({'id':'discTopic','type':'text','maxlength':'45'})
					)
			)
			.append(
				$('<p/>')
					.append(
						$('<button/>')
							.attr({'id':'createDiscussionButton'})
							.text('Create Discussion')
							.click(function(){
								$.discussion.insertDiscussion();
							})
					)
			);
		
		_form.dialog({
			'title': 'Create Discussion',
			'height': '175',
			'width': '600',
			'modal':'false',
			'closeOnEscape': 'true',
            'resizable': 'false'
		}).show();
		
	};
	
	$.discussion.insertDiscussion = function(){
		
		var _topic = $('#discTopic').val();
		
		
		if(_topic == '')
		{
			alert('Please enter a discussion topic.');
		}
		else
		{
			var gId = $.group.global.groupId;
			var gName = $('#GroupName').text();
			var dName = _topic;
			
			$.profile.global.currentRequest = 
				$.ajax({ 
			  	  	url:"/TheHive/DiscussionDataService",
			  	  	data:{
			  	  		"topic": _topic,
			  	  		"groupId":$.discussion.global.groupId,
			  	  		"personId":$.profile.global.PersonId,
			  	  		"method":"addTopic"
			  	  	},
					type:'POST',
					dataType:"json",
					success: function(html){
						//$('#globalSettings').data('searchResults',html);
						//$.profile.global.searchResults = html['searchResults'];
						$('#createDiscussionForm').dialog('destroy').remove();
						$.discussion.sendEmail(gId,gName,dName);
						alert("Discussion Topic Added Successfully!");
						$.discussion.buildView();
					},
					error: function(html){
						alert("An error occured while gathering profile information." +
								"\nPlease refresh your browser and try again.");
					}
				}); //Closes ajax
		}
	};
	
	$.discussion.sendEmail = function(gId,gName,dName){
		
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/DiscussionDataService",
		  	  	data:{
		  	  		"groupId": gId,
			  	  	"groupName": gName,
			  	  	"discussionName": dName,
		  	  		"method":"sendEmails"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('sendEmail',html);
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
		
	};
	
	$.discussion.gatherDiscussions = function(){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/DiscussionDataService",
		  	  	data:{
		  	  		"groupId":$.discussion.global.groupId,
		  	  		"method":"gatherTopics"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('discussionTopics',html);
					$.discussion.global.topics = html['discussionTopics'];
					//alert("Discussion Topic Added Successfully!");
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	$.discussion.buildCommentView = function(_selectedDiscussion){
		
		$.discussion.global.selectedDiscussionId = _selectedDiscussion.attr('id');
		$.discussion.gatherComments($.discussion.global.selectedDiscussionId);
		
		$('#processing').show();
		
		setTimeout(function(){
			
			$('#processing').hide();
			$('#discussionList').remove();
			$('#commentBar').remove();
			$('#groupProfileUpload').remove();
			$('#commentsDiv').remove();
			$('#editMembers').remove();
			$('#settingsView').remove();
			
			var _comments = $.discussion.global.comments;
			
			var _discussion = $.discussion.global.topics;
			
			for(var i=0;i<_discussion.length;i++)
			{
				if(_discussion[i]['Discussion ID'] == $.discussion.global.selectedDiscussionId)
				{
					$.discussion.global.selectedDiscussion = _discussion[i];
				}
			}
			
			var _selectedDiscussion = $.discussion.global.selectedDiscussion;
			
			$('#groupsRight')
				.append(
					$('<div/>')
						.attr({'id':'commentBar'})
						.append(
							$('<label/>')
								.attr({'id':'final'})
								.text('Reply To Topic')
								.css({'font-size':'13px','width':'140px'})
						)
						.click(function(){
							$.discussion.createComment();
						})
				)
				.append(
					$('<div/>')
						.attr({'id':'commentsDiv'})
						.append(
								$('<div/>')
									.attr({'id':''+_selectedDiscussion['Discussion ID']})
									.append(
										$('<div/>')
											.attr({'id':'topicTitle'})
											.append(
												$('<span/>')
													.attr({'id':'date'})
													.text(_selectedDiscussion['Create Date'])
											)
											.append(
												$('<span/>')
													.attr({'id':'title'})
													.text(_selectedDiscussion['Discussion Text'])
											)
											.append(
												$('<span/>')
													.attr({'id':'Author'})
													.text('Created By: '+ _selectedDiscussion['Person Name'])
											)
									)
									.dblclick(function(){
										$.discussion.buildCommentView($(this));
									})
							)
				);
			
			for(var i=0;i<_comments.length;i++)
			{
				var _curData = _comments[i];
				
				if(_curData['Associated Comment'] == 'NONE')
				{
					$('#commentsDiv')
						.append(
								$('<div/>')
									.attr({'id':_curData['Comment ID']})
									.css({'margin-left':'15px'})
									.append(
										$('<div/>')
											.attr({'id':'rootComment'})
											.append(
													$('<div/>')
													.attr({'id':'commentTitle','name':_curData['Comment ID']})
													.append(
														$('<span/>')
															.attr({'id':'date'})
															.text(_curData['Created Date'])
													)
													.append(
														$('<span/>')
															.attr({'id':'title'})
															.text(_curData['Comment Text'])
													)
													.append(
														$('<span/>')
															.attr({'id':'Author'})
															.text(_curData['Created By'])
													)
													.dblclick(function(){
														$.discussion.replyToComment($(this));
													})
											)
									)
									.append(
										$('<div/>')
											.attr({'id':'commentReply'+_curData['Comment ID']})
								)
						);
				}
			}
			
			$('#commentsDiv')
				.append(
					$('<label/>')
						.attr({'id':'note'})
						.text('*Double-Click on a comment to reply.')
				);
			
			$.discussion.gatherNestedComments();
			
		},1000);
		
	};
	
	$.discussion.gatherComments = function(discussionId){
		$.profile.global.currentRequest = 
			$.ajax({ 
		  	  	url:"/TheHive/DiscussionDataService",
		  	  	data:{
		  	  		"discussionId":discussionId,
		  	  		"method":"gatherComments"
		  	  	},
				type:'POST',
				dataType:"json",
				success: function(html){
					$('#globalSettings').data('discussionComments',html);
					$.discussion.global.comments = html['discussionComments'];
				},
				error: function(html){
					alert("An error occured while gathering profile information." +
							"\nPlease refresh your browser and try again.");
				}
			}); //Closes ajax
	};
	
	$.discussion.createComment = function(){
		$('#groupsRight')
			.append(
				$('<div/>')
					.attr({'id':'createCommentForm'})
			);
		
		var _form = $('#createCommentForm');
		
		_form.hide();
		
		
		_form
			.append(
				$('<p/>')
					.append(
						$('<label/>')
							.text('Your Comment: ')
					)
					.append(
						$('<textarea/>')
							.attr({'id':'commentInput','rows':'6','cols':'45'})
					)
			)
			.append(
				$('<button/>')
					.attr({'id':'createCommentButton'})
					.text('Create Comment')
					.click(function(){
						$.discussion.insertComment("root");
					})
			);
		
		_form.dialog({
			'title': 'Create Comment',
			'height': '350',
			'width': '550',
			'modal':'false',
			'closeOnEscape': 'true',
            'resizable': 'false'
		}).show();
		
	};
	
	$.discussion.replyToComment = function(comment){
		$('#groupsRight')
			.append(
				$('<div/>')
					.attr({'id':'createCommentForm'})
			);
		
		var _form = $('#createCommentForm');
		
		_form.hide();
		
		_form
			.append(
				$('<label/>')
					.attr({'id':comment.attr('name'),'name':'commentId'})
					.addClass('commentId')
					.hide()
			)
			.append(
				$('<p/>')
					.append(
						$('<label/>')
							.text('Your Comment: ')
					)
					.append(
						$('<textarea/>')
							.attr({'id':'commentInput','rows':'6','cols':'45'})
					)
			)
			.append(
				$('<button/>')
					.attr({'id':'createCommentButton'})
					.text('Create Comment')
					.click(function(){
						$.discussion.insertComment("comment");
					})
			);
		
		_form.dialog({
			'title': 'Create Comment',
			'height': '350',
			'width': '550',
			'modal':'false',
			'closeOnEscape': 'true',
	        'resizable': 'false'
		}).show();
	};
	
	
	$.discussion.insertComment = function(loc){
		var _comment = $('#commentInput').val();
		
		var gId = $.group.global.groupId;
		var gName = $('#GroupName').text();
		
		if(loc == "root")
		{
			$.profile.global.currentRequest = 
				$.ajax({ 
			  	  	url:"/TheHive/DiscussionDataService",
			  	  	data:{
			  	  		"discussionId":$.discussion.global.selectedDiscussionId,
			  	  		"commentText": _comment,
			  	  		"personId": $.profile.global.PersonId,
			  	  		"method":"createCommentFromRoot"
			  	  	},
					type:'POST',
					dataType:"json",
					success: function(html){
						$('#globalSettings').data('commentMessage',html);
						alert('Comment Created Successfully!');
						$('#createCommentForm').dialog('destroy').remove();
						$.discussion.sendEmail(gId,gName,_comment);
						$.discussion.buildView();
					},
					error: function(html){
						alert("An error occured while gathering profile information." +
								"\nPlease refresh your browser and try again.");
					}
				}); //Closes ajax
		}
		if(loc == "comment")
		{
			var _assocCommentId = $('.commentId').attr('id');
			
			$.profile.global.currentRequest = 
				$.ajax({ 
			  	  	url:"/TheHive/DiscussionDataService",
			  	  	data:{
			  	  		"discussionId":$.discussion.global.selectedDiscussionId,
			  	  		"commentText": _comment,
			  	  		"personId": $.profile.global.PersonId,
			  	  		"associateCommentId": _assocCommentId,
			  	  		"method":"createCommentFromComment"
			  	  	},
					type:'POST',
					dataType:"json",
					success: function(html){
						$('#globalSettings').data('commentMessage',html);
						//$.discussion.global.comments = html['discussionComments'];
						alert('Comment Created Successfully!');
						$('#createCommentForm').dialog('destroy').remove();
						$.discussion.buildView();
					},
					error: function(html){
						alert("An error occured while gathering profile information." +
								"\nPlease refresh your browser and try again.");
					}
				}); //Closes ajax
		}
	};
	
	
	$.discussion.gatherNestedComments = function(){
		var _comments = $.discussion.global.comments;
		
		for(var i=0;i<_comments.length;i++)
		{
			var _curData = _comments[i];
			
			if(_curData['Associated Comment'] != 'NONE')
			{
				var _newCommentDiv = 
					$('<div/>')
						.attr({'id':_curData['Comment ID']})
						.css({'margin-left':'15px'})
						.append(
							$('<div/>')
								.attr({'id':'replyComment','name':_curData['Comment ID']})
								.append(
										$('<div/>')
										.attr({'id':'commentTitle'})
										.append(
											$('<span/>')
												.attr({'id':'date'})
												.text(_curData['Created Date'])
										)
										.append(
											$('<span/>')
												.attr({'id':'title'})
												.text(_curData['Comment Text'])
										)
										.append(
											$('<span/>')
												.attr({'id':'Author'})
												.text(_curData['Created By'])
										)
								)
								.dblclick(function(){
									$.discussion.replyToComment($(this));
								})
						)
						.append(
								$('<div/>')
									.attr({'id':'commentReply'+_curData['Comment ID']})
						);
				
				$('#commentReply'+_curData['Associated Comment'])
					.append(
							_newCommentDiv
					);
			}
			
		}
		
	};
	
})(jQuery);