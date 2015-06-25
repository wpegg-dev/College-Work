package common;

public class Queries {

	public static final String LOGIN_GATHER_ACCOUNT_DATA =
			"SELECT " +
					"p.identifier AS PersonID, " +
					"p.first AS FirstName, " +
					"p.last AS LastName, " +
					"p.email AS EmailAdd, "+
					"p.repo_loc AS UserRepo, "+
					"p.profile_picture_loc AS ProfilePic " +
			"FROM " +
				"test.person AS p " +
			"WHERE " +
				"p.email=? " +
				"AND " +
				"p.password=?;";
	
	public static final String GATHER_UPDATED_USER_DATA = 
			"SELECT " +
					"p.identifier AS PersonID, " +
					"p.first AS FirstName, " +
					"p.last AS LastName, " +
					"p.email AS EmailAdd, "+
					"p.repo_loc AS UserRepo, "+
					"p.profile_picture_loc AS ProfilePic " +
			"FROM " +
				"test.person AS p " +
			"WHERE " +
				"p.identifier=? ;";
	
	public static final String CREATE_USER_ACCOUNT = 
			"INSERT INTO test.person "+
			"(first,last,email,password,repo_loc,profile_picture_loc,create_date,modify_date) "+
			"VALUES "+
			"(?,?,?,?,?,?,NOW(),NOW());";
	
	public static final String GATHER_SIMPLE_GROUP_DATA = 
			"SELECT  "+
				"g.id AS GroupId,  "+
				"g.name AS Name "+ 
			"FROM  "+
				"test.group AS g "+ 
				"INNER JOIN "+
					"test.group_members AS gm "+
					"ON "+
					"g.id = gm.groupId "+
			"WHERE "+
				"gm.personId = ?;";
	
	public static final String CREATE_GROUP = 
			"INSERT INTO test.group (name,class_title,group_description,repo_root) "+
			"VALUES (?,?,?,?);";
	
	public static final String CREATE_GROUP_ADMIN =
			"INSERT INTO test.group_admin (groupId,personId) "+
			"SELECT g.id,? FROM test.group AS g WHERE g.name = ?;";
	
	public static final String CREATE_GROUP_ADD_USER_TO_GROUP =
			"INSERT INTO  test.group_members (groupId,personId) "+
			"SELECT g.id,? FROM test.group AS g WHERE g.name = ?;";
	
	public static final String CREATE_DISCUSSION_TOPIC =
			"INSERT INTO test.discussion (description,group_id,person_id,create_date) "+
			"VALUES (?,?,?,NOW());";
	
	public static final String CREATE_USER_COMMENT_FROM_COMMENT = 
			"INSERT INTO test.comment (description,discussion_id,created_by,create_date,assoc_discussion_id) "+
			"VALUES (?,?,?,NOW(),?);";
	
	public static final String CREATE_USER_COMMENT_FROM_ROOT_DISCUSSION = 
			"INSERT INTO test.comment (description,discussion_id,created_by,create_date) "+
			"VALUES (?,?,?,NOW());";
	
	public static final String GATHER_COMMENTS = 
			"SELECT "+
				"c.id AS ID, "+
				"c.description AS Description, "+
				"c.discussion_id AS DiscussionID, "+
				"c.create_date AS CreatedDate, "+
				"CASE "+
					"WHEN c.assoc_discussion_id IS NULL THEN 'NONE' "+
					"ELSE c.assoc_discussion_id "+
				"END AS AssociatedComment, "+
				"CONCAT(p.first,' ',p.last) AS PersonName "+
			"FROM "+
				"test.comment AS c "+
				"INNER JOIN "+
					"test.person AS p "+
					"ON "+
					"c.created_by = p.identifier "+
			"WHERE "+
				"c.discussion_id = ? "+
			"ORDER BY "+
				"c.id;";
	
	public static final String GATHER_GROUP_DETAILS= 
			"SELECT "+ 
				"g.id AS ID, "+
				"g.name AS GroupName, "+
				"g.class_title AS ClassTitle, "+
				"g.repo_root AS Repository, " +
				"g.group_description AS Descr, "+
				"CONCAT(p.first,' ',p.last) AS AdminName, "+
				"g.profile_picture_loc AS ProfilePic "+
			"FROM  "+
				"test.group AS g "+
				"INNER JOIN "+
					"test.group_admin AS ga "+
					"ON "+
					"g.id = ga.groupId "+
				"INNER JOIN "+
					"test.person AS p "+
					"ON "+
					"ga.personId = p.identifier "+
			"WHERE  "+
				"g.id = ?; ";
	
	public static final String ADD_USER_TO_GROUP = 
			"INSERT INTO test.group_members (groupId,personId) "+
			"VALUES (?,?)";
	
	public static final String GATHER_GROUP_MEMBERS = 
			"SELECT   "+
				"p.identifier AS ID,  "+
				"CONCAT(p.first,' ',p.last) AS PersonName, "+
				"p.email AS EmailAddress "+ 
			"FROM   "+
				"test.group AS g "+ 
				"INNER JOIN "+
					"test.group_members AS gm "+
					"ON "+
					"g.id = gm.groupId "+
				"INNER JOIN  "+
					"test.person AS p "+ 
					"ON  "+
					"gm.personId = p.identifier "+ 
			"WHERE  "+
				"g.id = ?; ";
	
	public static final String GATHER_GROUP_DISCUSSION_TOPICS = 
			"SELECT "+
				"d.id AS ID, "+
				"d.description AS Topic, "+
				"d.group_id AS GroupId, "+
				"CONCAT(p.first,' ',p.last) AS PersonName, "+
				"d.create_date AS CreatedDate "+
			"FROM  "+
				"test.discussion AS d "+ 
				"INNER JOIN  "+
					"test.group AS g "+
					"ON "+
					"d.group_id = g.id "+
				"INNER JOIN "+
					"test.person AS p "+ 
					"ON "+
					"d.person_id = p.identifier "+
			"WHERE  "+
				"group_id = ?;";
	
	public static final String SEARCH_PEOPLE = 
					"SELECT p.identifier AS ItemId,CONCAT(p.first,' ',p.last) AS ItemName,'Person' AS ItemType, p.profile_picture_loc AS ProfilePic FROM test.person AS p "+ 
					"WHERE CONCAT(p.first,' ',p.last) LIKE ? GROUP BY p.identifier; ";
	
	public static final String SEARCH_GROUPS =
					"SELECT g.id AS ItemId, g.name AS ItemName,'Group' AS ItemType, g.profile_picture_loc AS ProfilePic  FROM test.group AS g "+
					"WHERE g.name LIKE ?; ";
	
	public static final String GATHER_PUBLIC_PROFILE_USER_DATA = 
			"SELECT p.identifier AS personId, p.first AS FirstName, p.last AS LastName, p.email AS EmailAddress, p.profile_picture_loc AS ProfilePic FROM test.person AS p WHERE identifier = ?;";
	
	public static final String GATHER_FRIENDS_LIST = 
			"SELECT  "+
				"friend.identifier AS FriendId, "+
				"friend.first AS FirstName, "+
				"friend.last AS LastName, "+
				"friend.email AS EmailAddress, "+
				"friend.profile_picture_loc AS ProfilePic "+
			"FROM   "+
				"test.friends AS f "+  
				"INNER JOIN  "+
					"test.person AS me "+ 
					"ON  "+
					"f.personId = me.identifier "+ 
				"INNER JOIN  "+
					"test.person AS friend "+ 
					"ON  "+
					"f.friendId = friend.identifier "+ 
			"WHERE personId =  ?;";
	
	public static final String ADD_PERSON_AS_FRIEND = 
			"INSERT INTO test.friends(personId,friendId,created_date) "
			+ "VALUES(?,?,NOW());";
	
	public static final String UPDATE_USER_SETTINGS = 
			"UPDATE test.person AS p SET p.first = ? , p.last = ? , p.email = ? , p.modify_date = NOW() WHERE p.identifier = ?";
	
	public static final String UPDATE_USER_SETTINGS_WITH_PASSWORD = 
			"UPDATE test.person AS p SET p.first = ? , p.last = ? , p.email = ? , p.password = ?, p.modify_date = NOW() WHERE p.identifier = ?";
	
	public static final String UPDATE_GROUP_SETTINGS = 
			"UPDATE test.group AS g SET g.name = ?, g.class_title = ?, g.group_description = ?"+ 
			"WHERE g.id = ?";
	
	public static final String REMOVE_MEMBER_FROM_GROUP = 
			"DELETE FROM test.group_members WHERE groupId = ? AND personId = ?;";
	
	public static final String UPDATE_USER_PROFILE_PICTURE = 
			"UPDATE test.person AS p SET p.profile_picture_loc = ?, p.modify_date=NOW() WHERE p.identifier=?;";
	
	public static final String UPDATE_GROUP_PROFILE_PICTURE = 
			"UPDATE test.group AS g SET g.profile_picture_loc = ? WHERE g.id = ?;";
	
}
