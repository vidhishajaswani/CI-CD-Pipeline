const fs = require('fs');
const path = require('path');
const Random = require('random-js');
const child_process = require('child_process');
var recursive = require("recursive-readdir");
var random = new Random(Random.engines.mt19937().seed(0));
child_process.execSync(`git checkout -B fuzzer`);
recursive("iTrust2/src/main/java/edu/ncsu/csc/itrust2", function (err, files) {
    
    
    files.forEach(function(fileName)
    {
        //console.log(fileName);
        var data = fs.readFileSync(fileName, 'utf-8');
        fs.writeFileSync(fileName,'','utf8');
        var lines = data.split("\n");
        //console.log(lines);
        lines.forEach(function(line)
        {
            //random = new Random(Random.engines.mt19937().seed(0));
            //make changes in the string
            if(random.bool(0.20))
            {
                var words=line.split(' ');
                words.forEach(function(word, index) 
                {

                    var match = word.match(/\"[\w|\d]*\"/i);
                    if(match!=null)
                    {
                        //console.log(match[0]);
                        var old = match[0].substring(1, match[0].length - 1);
                        var newWord = old;
                        //random = new Random(Random.engines.mt19937().seed(0));
                        //reverse the string
                        if(random.bool(0.10))
                        {
                            newWord=newWord.split('').reverse().join('');
                            //console.log(newWord);
                        }
                        //random = new Random(Random.engines.mt19937().seed(0));
                        //delete random characters
                        if(random.bool(0.50))
                        {
                            var array=newWord.split('');
                            var rand=random.integer(0,array.length);
                            var len=random.integer(0,array.length);
                            array.splice(rand,len);
                            newWord=array.join('');
                            //console.log(newWord);
                        }
                        words[index] = words[index].replace(match, "\""+newWord+"\"");
                        //console.log(words[index]);

                    }

                        
                    
                });
                line=words.join(' ');
                
                
            }
            //random = new Random(Random.engines.mt19937().seed(0));
            if(random.bool(0.25))
            {
                if(line.match('for') || line.match('while') || line.match('if'))
                {
                    //console.log('found < >');
                    if(line.match('<'))
                        line=line.replace('<','>');
                    else if(line.match('>') && !line.match('->'))
                        line=line.replace('>','<');

                    if(line.match(/\+\+/))
                        line=line.replace(/\+\+/g,'--');
                    else if(line.match('--'))
                        line=line.replace('--','++');

                }
                
            }
            //random = new Random(Random.engines.mt19937().seed(0));
            if(random.bool(0.15))
            {
                //console.log('found == !=');
                if(line.match('=='))
                {
                    line=line.replace(/==/g,'!=');
                    
                }
                else if(line.match('!='))
                    line=line.replace(/!=/g,'==');
                
            }
            //random = new Random(Random.engines.mt19937().seed(0));
            if(random.bool(0.20))
            {
                //console.log('found 0 1');
                if(line.match('0'))
                {
                    line=line.replace(/0/g,'1');
                    
                }
                else if(line.match('1'))
                    line=line.replace(/1/g,'0');
                
            }
            //random = new Random(Random.engines.mt19937().seed(0));
            if(random.bool(0.40))
            {
                //console.log('found 0 1');
                if(line.match('true'))
                {
                    line=line.replace('true','false');
                    
                }
                else if(line.match('false'))
                    line=line.replace('false','true');
                
            }
            if(line != '\r')
              line = line + '\n'
            fs.appendFileSync(fileName,line);
        });
        


    });
    
  });
  child_process.execSync(`git add . `);
  child_process.execSync(`git commit -m "fuzzer"`);
