var fs = require('fs'),
    xml2js = require('xml2js'),
    child  = require('child_process'); 
var parser = new xml2js.Parser();
var Bluebird = require('bluebird')

var testReport =  '/simplecalc/target/surefire-reports/TEST-com.github.stokito.unitTestExample.calculator.CalculatorTest.xml';
var testReports = ['/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIAppointmentRequestTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIDiagnosisTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIDrugTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIEmergencyRecordFormTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIEnumTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIFoodDiaryTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIGeneralCheckupTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIGeneralOphthalmologyTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIHospitalTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIICDCodeTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APILabProcedureTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APILogEntryTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APILOINCTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIOphthalmologySurgeryTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIPasswordTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIPatientTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIPersonnelTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIPrescriptionTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.apitest.APIUserTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.AppointmentRequestTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.DomainObjectTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.DrugTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.EmailUtilTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.FoodDiaryEntryTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.GeneralCheckupTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.GeneralOphthalmologyTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.HospitalFormTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.ICDCodeTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.LabProcedureTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.LockoutTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.LogEntryTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.LogTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.LOINCTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.OphthalmologySurgeryTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.PasswordChangeTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.PatientTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.PersonnelFormTest.xml',
'/home/vagrant/itrust.git/iTrust2/iTrust2/target/surefire-reports/TEST-edu.ncsu.csc.itrust2.unit.UserTest.xml']

if( process.env.NODE_ENV != "test")
{
    calculatePriority();
    //findFlaky();
}

async function findFlaky()
{
    var stats = {};
    for( var i = 0; i < 20; i++ )
    {
        try{
            child.execSync('cd simplecalc && mvn test');
        }catch(e){}
        var contents = fs.readFileSync(__dirname + testReport)
        let xml2json = await Bluebird.fromCallback(cb => parser.parseString(contents, cb));
        var tests = readResults(xml2json);
        //var stats = tests;
        for(let test of tests)
        {
            if(!stats.hasOwnProperty(test.name))
            {
                stats[test.name] = {passed: 0, failed: 0};
            }
            if(test.status == "passed")
                stats[test.name].passed++;
            if(test.status == "failed")
                stats[test.name].failed++; 
        }
        console.log(stats);
        //tests.forEach( e => console.log(i, e));
    }
}

/*function readResults(result)
{
    var tests = [];
    for( var i = 0; i < result.testsuite['$'].tests; i++ )
    {
        var testcase = result.testsuite.testcase[i];
        //var passed = testcase.passed;
        //var failed = testcase.failed;
        tests.push({
        name:   testcase['$'].name, 
        time:   testcase['$'].time, 
        status: testcase.hasOwnProperty('failure') ? "failed": "passed", 
        //passed: testcase.hasOwnProperty('falure') ? passed : passed+1,
        //failed: testcase.hasOwnProperty('falure') ? failed+1 : failed,
        //total:  passed+failed
        });
    }    
    return tests;
}*/

function readResults(result)
{
    var testObj = {};
    var tests = [];
    for( var i = 0; i < result.testsuite['$'].tests; i++ )
    {
        var testcase = result.testsuite.testcase[i];
        //var passed = testcase.passed;
        //var failed = testcase.failed;
        tests.push({
            name:   testcase['$'].name,
            time:   testcase['$'].time,
            status: testcase.hasOwnProperty('failure') ? "failed": "passed",
            /*passed: testcase.hasOwnProperty('falure') ? passed : passed+1,
            failed: testcase.hasOwnProperty('falure') ? failed+1 : failed,
            total:  passed+failed*/
        });
    }
    testObj["tests"] = tests;
    var passed = 0;
    for(var i=0; i<tests.length; i++) {
        if(tests[i].status == "passed") {
            passed++;
        }
    }
    testObj["passed"] = passed;
    return testObj;
}

/*function compare(a,b) {
    if (a.status < b.status)
      return -1;
    if (a.status > b.status)
      return 1;
    else if (a.status === "failed" && b.status === "failed") {
        var aTime = parseFloat(a.time);
        var bTime = parseFloat(b.time);
        if(aTime<bTime)
            return -1;
        else if(bTime<aTime)
            return 1;
        else 
            return 0;
    } 
    return 0;
}*/

function compare(a,b) {
    if(a.time < b.time)
        return -1;
    else
        return 1;
}

function compareAllTests (a,b) {
    if(a.passed > b.passed)
        return -1;
    else
        return 1;
}

async function calculatePriority()
{
    var finalResults = [];
    try{
        child.execSync('cd /home/vagrant/itrust.git/iTrust2/iTrust2 && mvn test');
    }catch(e){}
    for(var i=0; i<testReports.length; i++) {
        var contents = fs.readFileSync(testReports[i])
        let xml2json = await Bluebird.fromCallback(cb => parser.parseString(contents, cb));
        var tests = readResults(xml2json);
        tests.tests.sort(compare)
        //tests.forEach( e => console.log(e));
        finalResults.push(tests);
    }
    finalResults.sort(compareAllTests);
    finalResults.forEach(element => {
        console.log(element);
    });
    return tests;
}

module.exports.findFlaky = findFlaky;
module.exports.calculatePriority = calculatePriority;