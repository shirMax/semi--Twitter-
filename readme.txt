1) How to run our code:
	1.1) Command for Reactor server:
		mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args=<port> <Num of threads>
	     Command for TPC server:
		mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args=<port>
	     Command for Client:
		bin/BGSclient <ip> <port>
		
	1.2) Message types:
		Type		│Template						│Example
		────────────────┼───────────────────────────────────────────────────────┼────────────────────────────────
		Register	│REGISTER <Username> <Password> <Birthday (DD-MM-YYYY)>	│REGISTER client1 1234 01-01-1970
		Login		│LOGIN<Username> <Password> <Captcha (0/1)>		│LOGIN client1 1234 1
		Logout		│LOGOUT							│LOGOUT
		Follow		│FOLLOW <0/1 (Follow/Unfollow)> <UserName>		│FOLLOW 0 client1
		Post		│POST <PostMsg>						│POST Hello, world! @client1
		PM		│PM <Username> <Content>				│PM client1 Hellow, world!
		LOGSTAT		│LOGSTAT						│LOGSTAT
		STAT		│STAT <UserNames_list ('|' delimited)>			│STAT client1|client2|client3
		Block		│BLOCK <Username>					│BLOCK client1

2) Filtered set of words stored as LinkedList<String> in bgu/spl/net/srv/Cluster.java
