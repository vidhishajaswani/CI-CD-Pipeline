var fs = require('fs');
var testFolder = '../builds';
var testsData = {};
function getDirectories(path) {
    return fs.readdirSync(path).filter(function (file) {
        try {
            return fs.statSync(path+'/'+file).isDirectory();
        }
        catch(error) {
            return;
        }
    });
}
var directories = getDirectories(testFolder);
//console.log(directories);
for(keys in directories) {
    //console.log('hi');
    var currentFolder = testFolder+'/'+directories[keys];
    //console.log(currentFolder);
    var files = fs.readdirSync(currentFolder);
    //console.log(files)
    var separators = ['\n', ',', '{'];
    var currentFile = currentFolder+'/log';
    try {
        var array = fs.readFileSync(currentFile).toString().split(new RegExp('[' + separators.join('') + ']', 'g'));
    }
    catch(error) {
        continue;
    }
    structuredData(array)
}
//testsData.sort(compare);
/*keysSorted = Object
                .keys(testsData)
                .sort(function(a,b)
                {
                    return testsData[b][1]-testsData[a][1];
                });*/
keysSorted = Object
                .keys(testsData)
                .sort(function(a,b)
                {
                    if(testsData[a][1] == testsData[b][1])
                        return testsData[a][0]-testsData[b][0];
                    else 
                        return testsData[b][1]-testsData[a][1];
                });
//console.log(keysSorted);
for(var index in keysSorted)
    console.log(keysSorted[index], testsData[keysSorted[index]]);

function structuredData(array) {
    var slice = array.slice(0,978);
    var useful = [];
    //var test = {name: '', maxRuntime: 0.0, failureCount: 0}
    for(i in slice) {
        slice[i] = slice[i].replace('"','').replace('{','').replace('[','').replace(']','').replace('}','').trim();
        if (slice[i] != '' && !slice[i].includes('tests:') && !slice[i].includes('failed: '))
            if ((slice[i].includes('name:') || slice[i].includes('time:') || slice[i].includes('status:')) && !slice[i].includes('INFO'))
        {
            useful.push(slice[i]);
            // console.log(i+":", slice[i]);
        }
        //console.log(array2[i]);
    }
    for(i=0; i<useful.length; i+=3)
    {
        let runtime;
        let status;
        console.log(useful[i], useful[i+1], useful[i+2]);
        let testname = useful[i].split(' ')[1].replace('\'','').replace('\'','');
        if(useful[i+1] == undefined) {
            runtime = 0.0;
        }
        else {
            runtime = parseFloat(useful[i+1].split(' ')[1].replace('\'',''));
        }
        if(useful[i+2] == undefined) {
            status = 'passed';
        }
        else {
            status = useful[i+2].split(' ')[1].replace('\'','').replace('\'','');
        }
        // console.log(testname, runtime, status);
        let failure = 0;
        if (status == 'passed')
            failure = 0;
        else
            failure = 1;
        if (!testsData.hasOwnProperty(testname))
        {
            testsData[testname] = [runtime, failure];
        }
        else
        {
            if (testsData[testname][0] < runtime)
                testsData[testname][0] = runtime;
            testsData[testname][1] += failure;
        }
    }

    /*for(var key in testsData)
        console.log(testsData[key]);*/
}
