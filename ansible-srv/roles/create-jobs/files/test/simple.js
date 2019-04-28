const assert = require('assert');
const expect = require('chai').expect;
const main = require('../main')
const got   = require('got');
var http = require('http');
var request = require('request');
const execSync = require('child_process').execSync;
const awsEc2Ip = execSync('npm config get awsEc2Ip',
    { stdio: ['ignore', 'pipe', 'pipe'] }).toString().replace(/\n$/, '');
var checkbox_server_url = 'http://' + awsEc2Ip;
var end_point = checkbox_server_url + '/studies.html'

describe('Array', function() {
  describe('#indexOf()', function() {
    it('should return -1 when the value is not present', function(){
      assert.equal(-1, [1,2,3].indexOf(4));
    });
  });
});

describe('main', function() {
    describe('#start()', function() {
      it('should start server on port 9001', async () => {

          await main.start();

          const response = await got('http://localhost:9001', {timeout:500})
          describe('Checkbox run check', function () {
              it('Checkbox.io running', function (done) {
                http.get(checkbox_server_url, function (res) {
                  assert.equal(200, res.statusCode);
                  done();
                  });
                });
			  it('Hitting end point of checkbox to verify', function (done) {
				http.get(end_point, function (res) {
				  assert.equal(200, res.statusCode);
				  done();
				  });
				});
				
			  it('should properly create survey question', async () => {
					  const response = await got.post(checkbox_server_url + '/api/design/survey', {
						body: {
					"markdown": "{NumberQuestions:true}\n-----------\n### Multiple Choice Question (Check all that apply)\nA *description* for question.\nQuestions are created with headers (level 3).\n* Choice A\n* Choice B\n* Choice C"
				},
						json: true,
						timeout: 500
					  });

					  expect(response.body.preview).to.include('Multiple Choice Question');
				});
			 it('study listing', function(done){
					request(checkbox_server_url + '/api/study/listing', function(error, response, body) {
						expect(response.statusCode).to.equal(200);
						done();
					});
				});

            });
			
          // Stop server
          await main.stop();
          expect(response.body).to.include('ok');
      });
    });
});
