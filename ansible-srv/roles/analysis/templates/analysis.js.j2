var esprima = require("esprima");
var options = {tokens:true, tolerant: true, loc: true, range: true };
var fs = require("fs");
var findInFiles = require("find-in-files");
var jsinspect = require('jsinspect');
var filepaths = require('filepaths');

function main()
{
	var args = process.argv.slice(2);

	if( args.length == 0 )
	{
		args = ["{{checkbox_repo}}", "analysis.js", 100, 70];		
	}
	var searchPath = args[0];
	var filePath = args[1];
	var MaxLines = args[2];
	var MaxConditions = args[3];

	complexity(filePath);

	// Report
	// 1. & 2. Check for Max Conditions and Number of Lines
	console.log("------------------------------------------------------------");
	console.log("Custom Analysis - Method Analysis");
	for( var node in builders )
	{
		var builder = builders[node];
		builder.report();
		builder.Analyze(MaxLines, MaxConditions);
	}

	// 3. Detect Duplicate code
	console.log("------------------------------------------------------------");
	console.log("Custom Analysis - Detection of Duplicate or Structurally similar code");
	var matches = detectDuplicates('.', 30);
	if(matches > 0)
	{
		console.log("Analysis Error: Duplicate or Structurally similar detected - Modify code and resubmit");
		process.exit(1);
	}

	// 4. Check for security tokens in the code 
	console.log("------------------------------------------------------------");
	console.log("Custom Analysis - Detection of Security Tokens")
	findInFiles.find("token", searchPath, ".js$")
    .then(function(results) {
		var count = 0;
        for (var result in results) {
            var res = results[result];
            console.log(
                '!WARNING! Found "' + res.matches[0] + '" ' + res.count
                + ' times in "' + result + '"'
			);
			count += res.count;
		}
		if (count == 0)
			console.log("No matches found");
		else
		{
			console.log("Analysis Error: Security Token detected - Modify code and resubmit");
			process.exit(1);
		}
	});
}

var builders = {};

// Represent a reusable "class" following the Builder pattern.
function FunctionBuilder()
{
	this.StartLine = 0;
	this.EndLine = 0;
	this.Lines = 0;
	this.FunctionName = "";
	// The number of parameters for functions
	this.ParameterCount  = 0,
	// Number of if statements/loops + 1
	this.SimpleCyclomaticComplexity = 0;
	// The max depth of scopes (nested ifs, loops, etc)
	this.MaxNestingDepth    = 0;
	// The max number of conditions if one decision statement.
	this.MaxConditions      = 0;
	this.childrenLength = 0;
	this.Analyze = function(MaxLines, MaxConditions)
	{
		if(this.MaxConditions > MaxConditions) 
		{
			console.log("Analysis Error: MaxCondition limit exceeded - Modify code and resubmit");
			console.log(this.FunctionName);
			process.exit(1);
		}
		if(this.Lines > MaxLines)
		{
			console.log("Analysis Error: MaxLines limit exceeded - Modify code and resubmit");
			console.log(this.FunctionName);
			process.exit(1);
		}
	}
	this.report = function()
	{
		console.log(
		   (
		   	"{0}(): {1}\n" +
			   "============\n" +
			   "Lines: {2}\t" +
				"MaxConditions: {3}\t" +
				"Children: {4}\n"
			)
			.format(this.FunctionName, this.StartLine,
			        this.Lines, this.MaxConditions, this.childrenLength)
		);
	}
};

// A builder for storing file level information.
function FileBuilder()
{
	this.FileName = "";
	// Number of strings in a file.
	this.Strings = 0;
	// Number of imports in a file.
	this.ImportCount = 0;
	this.Functions = [];
	this.CheckDuplicates = function()
	{
		for (node_a in this.Functions)
			for (node_b in this.Functions)
			{
				var result = isSameNode(node_a, node_b);
				if (result)
				{
					console.log("Duplication detected");
				}
			}
	}
	this.report = function()
	{
		// console.log (
			// ( "{0}\n" +
			//   "~~~~~~~~~~~~\n"+
			//   "ImportCount {1}\t" +
			//   "Strings {2}\n"
			// ).format( this.FileName, this.ImportCount, this.Strings));
		// this.CheckDuplicates();
	}
	this.Analyze = function(){}
}

// A function following the Visitor pattern.
// Annotates nodes with parent objects.
function traverseWithParents(object, visitor)
{
    var key, child;

    visitor.call(null, object);

    for (key in object) {
        if (object.hasOwnProperty(key)) {
            child = object[key];
            if (typeof child === 'object' && child !== null && key != 'parent') 
            {
            	child.parent = object;
					traverseWithParents(child, visitor);
            }
        }
    }
}

function complexity(filePath)
{
	var buf = fs.readFileSync(filePath, "utf8");
	var ast = esprima.parse(buf, options);

	var i = 0;

	// A file level-builder:
	var fileBuilder = new FileBuilder();
	fileBuilder.FileName = filePath;
	fileBuilder.ImportCount = 0;
	builders[filePath] = fileBuilder;

	// Tranverse program with a function visitor.
	traverseWithParents(ast, function (node) 
	{
		if (node.type === 'FunctionDeclaration') 
		{
			var builder = new FunctionBuilder();

			builder.FunctionName = functionName(node);
			builder.StartLine    = node.loc.start.line;
			builder.EndLine 	 = node.loc.end.line;

			// 2. Number of lines in a method
			builder.Lines = builder.EndLine - builder.StartLine;

			// if (node.params.length > 0)
			// {
			// 	builder.ParameterCount = node.params.length;
			// }
			builder.childrenLength = childrenLength(node);
			// 1. Max Conditions in a statement
			traverseWithParents(node, function(child)
			{
				if (isDecision(child))
				{
					builder.MaxConditions = countConditions(child);
				}
			});
			builders[builder.FunctionName] = builder;
			fileBuilder.Functions.push(node);
		}
		builders[filePath] = fileBuilder;
	});

}
count = 0;
// Helper function for counting conditions in a Decision statement node.
function countConditions(node)
{
	count = 0;
	traverseWithParents(node, function(child)
	{
		if(child.operator)
		{
			if(child.operator == '&&' || child.operator == '||')
			{
				count++;
			}
		}
	});
	return count+1;
}

// 4. Helper function to determine node similarity
// function isSameNode(node_a, node_b)
// {
	// if (node_a == null && node_b == null)
	// 	return true;
	// if (node_a == null || node_b == null)
	// 	return false;
	// else
	// {
	// 	let result = true && (node_a.type == node_b.type) && (Object.keys(node_a).length == Object.keys(node_b).length);
	// 	// Count is same, then call isSameNode(child_a, child_b)
	// 	// return the final result
	// 	var key_a, child_a, child_b;
	// 	if (result)
	// 		for (key_a in node_a) 
	// 		{
	// 			if (node_a.hasOwnProperty(key_a) && node_b.hasOwnProperty(key_a)) 
	// 			{
	// 				child_a = node_a[key_a];
	// 				child_b = node_b[key_a];
	// 				if (typeof child_a === 'object' && typeof child_b === 'object' && child_a !== null && child_b !== null && key_a != 'parent') 
	// 				{
	// 					result = result && isSameNode(child_a, child_b);
	// 				}
	// 			}
	// 		}
	// 	return result;
	// }
	// return Object.is(node_a, node_b);
// }

// Helper function for counting children of node.
function childrenLength(node)
{
	var key, child;
	var count = 0;
	for (key in node) 
	{
		if (node.hasOwnProperty(key)) 
		{
			child = node[key];
			if (typeof child === 'object' && child !== null && key != 'parent') 
			{
				count++;
			}
		}
	}
	return count;
}


// Helper function for checking if a node is a "decision type node"
function isDecision(node)
{
	if( node.type == 'IfStatement' || node.type == 'ForStatement' || node.type == 'WhileStatement' ||
		 node.type == 'ForInStatement' || node.type == 'DoWhileStatement')
	{
		return true;
	}
	return false;
}

// Helper function for printing out function name.
function functionName( node )
{
	if( node.id )
	{
		return node.id.name;
	}
	return "anon function @" + node.loc.start.line;
}

// Helper function for allowing parameterized formatting of strings.
if (!String.prototype.format) {
  String.prototype.format = function() {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function(match, number) { 
      return typeof args[number] != 'undefined'
        ? args[number]
        : match
      ;
    });
  };
}

main();

function Crazy (argument) 
{

	var date_bits = element.value.match(/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/);
	var new_date = null;
	if(date_bits && date_bits.length == 4 && parseInt(date_bits[2]) > 0 && parseInt(date_bits[3]) > 0)
    new_date = new Date(parseInt(date_bits[1]), parseInt(date_bits[2]) - 1, parseInt(date_bits[3]));

    var secs = bytes / 3500;

      if ( secs < 59 )
      {
          return secs.toString().split(".")[0] + " seconds";
      }
      else if ( secs > 59 && secs < 3600 )
      {
          var mints = secs / 60;
          var remainder = parseInt(secs.toString().split(".")[0]) -
(parseInt(mints.toString().split(".")[0]) * 60);
          var szmin;
          if ( mints > 1 )
          {
              szmin = "minutes";
          }
          else
          {
              szmin = "minute";
          }
          return mints.toString().split(".")[0] + " " + szmin + " " +
remainder.toString() + " seconds";
      }
      else
      {
          var mints = secs / 60;
          var hours = mints / 60;
          var remainders = parseInt(secs.toString().split(".")[0]) -
(parseInt(mints.toString().split(".")[0]) * 60);
          var remainderm = parseInt(mints.toString().split(".")[0]) -
(parseInt(hours.toString().split(".")[0]) * 60);
          var szmin;
          if ( remainderm > 1 )
          {
              szmin = "minutes";
          }
          else
          {
              szmin = "minute";
          }
          var szhr;
          if ( remainderm > 1 )
          {
              szhr = "hours";
          }
          else
          {
              szhr = "hour";
              for ( i = 0 ; i < cfield.value.length ; i++)
				  {
				    var n = cfield.value.substr(i,1);
				    if ( n != 'a' && n != 'b' && n != 'c' && n != 'd'
				      && n != 'e' && n != 'f' && n != 'g' && n != 'h'
				      && n != 'i' && n != 'j' && n != 'k' && n != 'l'
				      && n != 'm' && n != 'n' && n != 'o' && n != 'p'
				      && n != 'q' && n != 'r' && n != 's' && n != 't'
				      && n != 'u' && n != 'v' && n != 'w' && n != 'x'
				      && n != 'y' && n != 'z'
				      && n != 'A' && n != 'B' && n != 'C' && n != 'D'
				      && n != 'E' && n != 'F' && n != 'G' && n != 'H'
				      && n != 'I' && n != 'J' && n != 'K' && n != 'L'
				      && n != 'M' && n != 'N' &&  n != 'O' && n != 'P'
				      && n != 'Q' && n != 'R' && n != 'S' && n != 'T'
				      && n != 'U' && n != 'V' && n != 'W' && n != 'X'
				      && n != 'Y' && n != 'Z'
				      && n != '0' && n != '1' && n != '2' && n != '3'
				      && n != '4' && n != '5' && n != '6' && n != '7'
				      && n != '8' && n != '9'
				      && n != '_' && n != '@' && n != '-' && n != '.' )
				    {
				      window.alert("Only Alphanumeric are allowed.\nPlease re-enter the value.");
				      cfield.value = '';
				      cfield.focus();
				    }
				    cfield.value =  cfield.value.toUpperCase();
				  }
				  return;
          }
          return hours.toString().split(".")[0] + " " + szhr + " " +
mints.toString().split(".")[0] + " " + szmin;
      }
  }

  function detectDuplicates(suppliedPaths, thresh)
  {
	  // By default, ignore deps and tests
	  var ignorePatterns = ['node_modules', 'bower_components', 'test', 'spec'];
	  var extensions = ['.js', '.jsx'];
	  try {
		  paths = filepaths.getSync(suppliedPaths, {
		  ext: extensions,
		  ignore: ignorePatterns
		  });
	  } catch(e) {
		  console.log(e.message);
		  process.exit(4);
	  }
  
	  if (!paths.length) {
		  console.log('No files found in the list of paths');
		  process.exit(0);
	  }
  
	  var inspector = new jsinspect.Inspector(paths, {
		  threshold:    thresh
		});
  
	  // Retrieve the requested reporter
	  var reporterType = jsinspect.reporters['default'];
	  new reporterType(inspector);
  
	  // Track the number of matches
	  matches = 0;
	  inspector.on('match', function() {matches++});
  
	  try {
		  inspector.run();
		//   process.exit(5);
	  } catch(err) {
		  console.log("Error");
		  process.exit(1);
	  }

	  return matches;
  }
  
 exports.main = main;