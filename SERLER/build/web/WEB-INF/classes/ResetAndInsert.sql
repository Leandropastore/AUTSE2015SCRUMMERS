DROP TABLE IF EXISTS Accounts;
DROP TABLE IF EXISTS AllArticles;
DROP TABLE IF EXISTS RejectedArticles;
DROP TABLE IF EXISTS PorcessedDetails;
DROP TABLE IF EXISTS AuthorTable;
DROP TABLE IF EXISTS CredibilityTable;
DROP TABLE IF EXISTS MethodologyTable;
DROP TABLE IF EXISTS PracticeTable;
DROP TABLE IF EXISTS EvidenceItemTable;
DROP TABLE IF EXISTS ConfidenceTable;
DROP TABLE IF EXISTS ResearchDesignTable;
DROP TABLE IF EXISTS MetricTable;


CREATE TABLE Accounts
(
    AccountName varchar(255) NOT NULL UNIQUE,
    Password varchar(255),
    Email varchar(255),
    AccountType varchar(255),
    PRIMARY KEY (AccountName)
);


CREATE TABLE AllArticles
(
    ArticleId int NOT NULL UNIQUE AUTO_INCREMENT,
    Title varchar(255) NOT NULL,
    Authors varchar(255) NOT NULL,
    Journal varchar(255),
    YearOfPublish int,
    ResearchLv varchar(255),
    Status varchar(255) DEFAULT "new",
    RejectedReason varchar(8000) DEFAULT "",
    Contributor varchar(255),
    ModeratedBy varchar(255) DEFAULT "",
    AnalysedBy varchar(255) DEFAULT "",
    PRIMARY KEY (ArticleId)
);

CREATE TABLE RejectedArticles
(
    ArticleId int NOT NULL UNIQUE,
    Title varchar(255) NOT NULL,
    Moderator varchar(255),
    Reason varchar(8000),
    PRIMARY KEY (ArticleId)
);

CREATE TABLE CredibilityTable
(
    ArticleID int NOT NULL,
    Rater varchar(255) NOT NULL,
    Rating int,
    Reason varchar(255),
    PRIMARY KEY (ArticleID,Rater)
);

CREATE TABLE MethodologyTable
(
    ArticleID int NOT NULL,
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (ArticleID)
);


CREATE TABLE PracticeTable
(
    ArticleID int NOT NULL,
    M_Name varchar(255) NOT NULL,
    Description varchar(8000),
    PRIMARY KEY (ArticleID)
);


CREATE TABLE EvidenceItemTable
(
    ArticleID int NOT NULL,
    iName varchar(255) NOT NULL,
    iBenefit varchar(4000) DEFAULT "",
    iWho varchar(255) DEFAULT "",
    iWhat varchar(255) DEFAULT "",
    iWhere varchar(255) DEFAULT "",
    iWhen varchar(255) DEFAULT "",
    iHow varchar(255) DEFAULT "",
    iWhy varchar(255) DEFAULT "",
    iResult varchar(4000) DEFAULT "",
    iIntegrity varchar(4000) DEFAULT "",
    PRIMARY KEY (ArticleID)
);


CREATE TABLE ConfidenceTable
(
    ArticleID int NOT NULL,
    ItemName varchar(255) NOT NULL,
    Rater varchar(255) NOT NULL,
    Rating int,
    Reason varchar(255),
    PRIMARY KEY (ArticleID, ItemName, Rater)
);


CREATE TABLE ResearchDesignTable
(
    ArticleID int NOT NULL,
    qName varchar(255) NOT NULL,
    qDescription varchar(4000),
    methodName varchar(255),
    methodDescription varchar(4000),
    metricsName varchar(255),
    metricsDescription varchar(4000),
    participants varchar(255),
    PRIMARY KEY (ArticleID, qName)
);


INSERT INTO Accounts 
VALUES(
    "admin",
    "123",
    "admin@email.com",
    "administrator"
);
INSERT INTO Accounts 
VALUES(
    "analyst",
    "123",
    "analyst@email.com",
    "analyst"
);
INSERT INTO Accounts 
VALUES(
    "contributor",
    "123",
    "contributor@email.com",
    "contributor"
);
INSERT INTO Accounts 
VALUES(
    "moderator",
    "123",
    "moderator@email.com",
    "moderator"
);

/* 
Enter info for a suitable research paper
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
/* 
Empirical investigation of refactoring effect on software quality
*/

INSERT INTO AllArticles (ArticleID, Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)VALUES ('51', 'Empirical investigation of refactoring effect on software quality', 'Mohammad Alshayeb', 'Information and Software Technology', '2009', 'Level 1', 'new','contributor');
INSERT INTO CredibilityTable VALUES('51', 'contributor', '3','It is a good article');
Update allarticles SET Status = 'accepted' ,RejectedReason = '' ,ModeratedBy = 'moderator' WHERE ArticleId = '51';
INSERT INTO methodologytable VALUES  ('51', 'Other', 'Conducting experiment by performed refactoring on software')ON DUPLICATE KEY UPDATE  M_Name = VALUES(M_Name),  Description = VALUES(Description);
INSERT INTO Practicetable VALUES  ('51', 'Refactoring', 'Refactoring is the process of improving the design of existing code by changing its internal structure without affecting its external behavior')ON DUPLICATE KEY UPDATE  M_Name = VALUES(M_Name),  Description = VALUES(Description);
INSERT INTO EvidenceItemTable VALUES ('51', 'research on refactoring', 'improvement on software quality', 'Mohammad Alshayeb and his students', 'performed refactoring on three softwares', 'their lab', '2009', 'measure the quality of the software before and after refactoring', 'to find out if the software quality is improved', 'there in not conclusive evidence to support that refactoring improve software qualiy', 'just research on three software is no enough, further investigation is required ')ON DUPLICATE KEY UPDATE  iName = VALUES(iName),  iBenefit = VALUES(iBenefit),  iWho = VALUES(iWho),  iWhat = VALUES(iWhat),  iWhere = VALUES(iWhere),  iWhen = VALUES(iWhen),  iHow = VALUES(iHow),  iWhy = VALUES(iWhy),  iResult = VALUES(iResult),  iIntegrity = VALUES(iIntegrity);
INSERT INTO ConfidenceTable VALUES ('51', 'research on refactoring', 'analyst', '3', 'the amount of data is no enough');
INSERT INTO ResearchDesignTable VALUES ('51', 'refactoring does not improve software quality', 'investigate the effect of refactoring on software quality', 'practical', 'perform refactoring on three software', 'Quality', 'adaptability, maintainability, understandability,\r\nreusability, and testability.', 'Professor and Students')ON DUPLICATE KEY UPDATE  qName = VALUES(qName),  qDescription = VALUES(qDescription),  methodName = VALUES(methodName),  methodDescription = VALUES(methodDescription),  metricsName = VALUES(metricsName),  metricsDescription = VALUES(metricsDescription),  participants = VALUES(participants);
Update allarticles SET Status = 'analysed' ,AnalysedBy = 'analyst' WHERE ArticleId = '51';
Update allarticles SET Status = 'released' WHERE ArticleId = '51';

/* 
Determinants of software quality: A survey of information systems project managers
*/
INSERT INTO AllArticles (ArticleID,Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)VALUES ('52', 'Determinants of software quality: A survey of information systems project managers', 'Narasimhaiah Gorla, Shang-Che Lin', 'Information and Software Technology', '2009', 'Level 2', 'new','contributor');
INSERT INTO CredibilityTable VALUES('52', 'contributor', '4','it help me understand the software quality');
Update allarticles SET Status = 'accepted' ,RejectedReason = '' ,ModeratedBy = 'moderator' WHERE ArticleId = '52';
INSERT INTO methodologytable VALUES  ('52', 'Other', 'conducting a Survey')ON DUPLICATE KEY UPDATE  M_Name = VALUES(M_Name),  Description = VALUES(Description);
INSERT INTO Practicetable VALUES  ('52', 'Other', 'Software quality - how can we define the quality of a software')ON DUPLICATE KEY UPDATE  M_Name = VALUES(M_Name),  Description = VALUES(Description);
INSERT INTO EvidenceItemTable VALUES ('52', 'Data from a survey', 'determine what the software quality is ', 'Narasimhaiah Gorla, Shang-Che Lin', 'software quality', 'USA', '2009', 'conducting survey to the project managers of IS ', 'those project have experience on software quality', 'list of software quality factors is determined', 'the research have high integrity')ON DUPLICATE KEY UPDATE  iName = VALUES(iName),  iBenefit = VALUES(iBenefit),  iWho = VALUES(iWho),  iWhat = VALUES(iWhat),  iWhere = VALUES(iWhere),  iWhen = VALUES(iWhen),  iHow = VALUES(iHow),  iWhy = VALUES(iWhy),  iResult = VALUES(iResult),  iIntegrity = VALUES(iIntegrity);
INSERT INTO ConfidenceTable VALUES ('52', 'Data from a survey', 'analyst', '5', 'it is very detailed');
INSERT INTO ResearchDesignTable VALUES ('52', 'how to determine the software quality', 'how can software quality be measured', 'theoretical', 'ask the IS project managers list of questions to get their idea of software quality', 'survey data', 'information gather from the survey', 'Other')ON DUPLICATE KEY UPDATE  qName = VALUES(qName),  qDescription = VALUES(qDescription),  methodName = VALUES(methodName),  methodDescription = VALUES(methodDescription),  metricsName = VALUES(metricsName),  metricsDescription = VALUES(metricsDescription),  participants = VALUES(participants);
Update allarticles SET Status = 'analysed' ,AnalysedBy = 'analyst' WHERE ArticleId = '52';
Update allarticles SET Status = 'released' WHERE ArticleId = '52';

/*
The Making of Cloud Applications An Empirical Study on Software Development for the Cloud
*/
INSERT INTO AllArticles (ArticleID,Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)VALUES ('53', 'The Making of Cloud Applications An Empirical Study on Software Development for the Cloud', 'Jurgen Cito, Philipp Leitner, Thomas Fritz, Harald C. Gall', 'arXiv.org', '2014', 'Level 2', 'new','contributor');
INSERT INTO CredibilityTable VALUES('53', 'contributor', '4','it help me understand the software quality');
Update allarticles SET Status = 'accepted' ,RejectedReason = '' ,ModeratedBy = 'moderator' WHERE ArticleId = '53';
INSERT INTO methodologytable VALUES ('53', 'Other', 'conducting interveiw and Survey') ON DUPLICATE KEY UPDATE  M_Name = VALUES(M_Name),  Description = VALUES(Description);
INSERT INTO Practicetable VALUES ('53', 'Cloud Computing', 'Cloud computing allows application software to be operated using internet-enabled devices. Clouds can be classified as public, private, and hybrid')ON DUPLICATE KEY UPDATE  M_Name = VALUES(M_Name),  Description = VALUES(Description);
INSERT INTO EvidenceItemTable VALUES ('53', 'Data from interview and survey', 'understand the inter-relation between cloud computing and software development', ' Jurgen Cito, Philipp Leitner, Thomas Fritz, Harald C. Gall', 'cloud computing and software development', 'around the world', '2014', 'interview with experienced developers, survey to the github user ', 'knowing how the people think about cloud computing and software development', 'collected information from the interview and survey', 'the research have high integrity')ON DUPLICATE KEY UPDATE  iName = VALUES(iName),  iBenefit = VALUES(iBenefit),  iWho = VALUES(iWho),  iWhat = VALUES(iWhat),  iWhere = VALUES(iWhere),  iWhen = VALUES(iWhen),  iHow = VALUES(iHow),  iWhy = VALUES(iWhy),  iResult = VALUES(iResult),  iIntegrity = VALUES(iIntegrity);
INSERT INTO ConfidenceTable VALUES ('53', 'Data from interview and survey', 'analyst', '5', 'it is very detailed');
INSERT INTO ResearchDesignTable VALUES ('53', 'How cloud computing affect software development', 'How do processes, communication and collaboration change for professional software developers when moving to the cloud? What kind of tools and data do developers utilize for building cloud software? How does cloud computing impact how developers design and architect applications?', ' practical', 'conduting interview and survey', 'interview and survey data', 'information gather from the interview and survey', 'Other')ON DUPLICATE KEY UPDATE  qName = VALUES(qName),  qDescription = VALUES(qDescription),  methodName = VALUES(methodName),  methodDescription = VALUES(methodDescription),  metricsName = VALUES(metricsName),  metricsDescription = VALUES(metricsDescription),  participants = VALUES(participants);
Update allarticles SET Status = 'analysed' ,AnalysedBy = 'analyst' WHERE ArticleId = '53';
Update allarticles SET Status = 'released' WHERE ArticleId = '53';


/* TESTING DATA*/
INSERT INTO AllArticles (ArticleID, Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)VALUES ('100', 'This is a New article', 'New auther', 'New Journal', '2015', 'Level 1', 'new','contributor');
INSERT INTO CredibilityTable VALUES('100', 'contributor', '1','it used to test the list for moderate');
INSERT INTO AllArticles (ArticleID, Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)VALUES ('101', 'this is Accepted article', 'Accepted author', 'Accepted journal', '2015', 'Level 1', 'new','contributor');
INSERT INTO CredibilityTable VALUES('101', 'contributor', '1','it used to test the list for analyse');
Update allarticles SET Status = 'accepted' ,RejectedReason = '' ,ModeratedBy = 'moderator' WHERE ArticleId = '101';

INSERT INTO AllArticles (ArticleID, Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)VALUES ('102', 'this is Rejected article', 'Rejected author', 'Rejected journal', '2015', 'Level 1', 'new','contributor');
INSERT INTO CredibilityTable VALUES('102', 'contributor', '1','it used to test rejection');
Update allarticles SET Status = 'rejected' ,RejectedReason = 'just testing the rejection' ,ModeratedBy = 'moderator' WHERE ArticleId = '102';
/*
INSERT INTO AllArticles(Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor, ModeratedBy, AnalysedBy)
VALUES(
        "Empirical investigation of refactoring effect on software quality",
        "Mohammad Alshayeb",
        "Information and Software Technology",
        "2009",
        "Level 1",
        "released",
        "admin",
        "andy li",
        "andy li"
);

INSERT INTO AllArticles(Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor, ModeratedBy, RejectedReason)
VALUES(
        "A Rejected Article",
        "rejected author",
        "rejected journal",
        "1000",
        "Level 1",
        "rejected",
        "admin",
        "andy li",
        "just testing the rejection"
);

INSERT INTO AllArticles(Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)
VALUES(
        "this is a FAKE article",
        "FAKE author",
        "FAKE journal",
        "1000",
        "Level 1",
        "new",
        "admin"
);

INSERT INTO RejectedArticles 
VALUES (
    "2",
    "A Rejected Article",
    "admin",
    "I hate this article"
);


INSERT INTO CredibilityTable 
VALUES(
    "1",
    "Andy Li",
    "4",
    "I just want to give him a 4."
);

INSERT INTO CredibilityTable 
VALUES(
    "1",
    "Bob",
    "5",
    "I love the number 5."
);


INSERT INTO MethodologyTable 
VALUES(
    "1",
    "SuperMethod",
    "We cannot describe it"
);

INSERT INTO PracticeTable 
VALUES(
    "1",
    "Refactoring",
    "Refactoring is the process of improving the design of existing code by changing its internal structure without affecting its external behavior"
);

INSERT INTO EvidenceItemTable 
VALUES(
    "1",
    "Item A",
    "There is no benifit",
    "someone",
    "something",
    "somewhere",
    "2009-01-01",
    "somehow",
    "somewhy",
    "Some results",
    "Some integrity"
);

INSERT INTO ConfidenceTable 
VALUES(
    "1",
    "Item A",
    "Andy Li",
    "5",
    "I just want to give him a 5."
);

INSERT INTO ResearchDesignTable 
VALUES(
    "1",
    "What is this?",
    "talk about this",
    "XP",
    "what is XP",
    "Metric A",
    "Some sort of metric",
    "Professor"
);
*/