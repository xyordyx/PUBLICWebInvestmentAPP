web: java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war
heroku config:add java_opts='-Xmx384m -Xms384m -Xss512k -XX:+UseCompressedOops'
heroku config:add JAVA_OPTS='-Xmx384m -Xms384m -Xss512k -XX:+UseCompressedOops'