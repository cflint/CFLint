component persistent="true" moshen:displayLabel="${courseNumber} ${shortName}" moshen:listcolumns="coursenumber,name,shortname,department" cacheuse="nonstrict-read-write"{
	property name="courseid" ormtype="int" notnull="true" length="10" fieldtype="id" generator="identity";
	property name="courseNumber" ormtype="string" notnull="true" length="8";
	property name="name" ormtype="string" notnull="true" length="254";
	property name="shortName" ormtype="string" notnull="false" length="100";
	property name="Department" ormtype="int" length="10" fieldtype="many-to-one" column="departmentId" cfc="Department" fkcolumn="departmentId" notnull="true" lazy="true";
	property name="gradeMode" ormtype="int" length="10" fieldtype="many-to-one" column="id" cfc="CourseGradeMode" fkcolumn="gradeModeId" notnull="true" lazy="true";
	property name="Offering" type="array" fieldtype="one-to-many" missingrowIgnored="true" cfc="Offering" fkcolumn="courseID" lazy="true";
	property name="description" ormtype="text" notnull="true";
	property name="credit" ormtype="decimal" length="10";
	property name="live" ormtype="bit" type="boolean";

	/**
	 * @moshen:suppress true
	 **/
	property name="createDate" ormtype="datetime";
	/**
	 * @moshen:suppress true
	 **/
	property name="createdByUser" fieldtype="many-to-one" column="userId"  missingrowIgnored="true" fkcolumn="createdByUserId" cfc="User" lazy="true" orderby="userName";
	/**
	 * @moshen:suppress true
	 **/
	property name="modifiedByUser" fieldtype="many-to-one" column="userId" missingrowIgnored="true" fkcolumn="modifiedByUserId" cfc="User" lazy="true" orderby="userName";
	/**
	 * @moshen:suppress true
	 **/
	property name="modDate" ormtype="datetime";
}