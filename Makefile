default:
	javac -d ./bin src/plot/*.java src/assignment8AveAmplitude/*.java
clean:
	$(RM) ./bin/plot/*.class
	$(RM) ./bin/assignment8AveAmplitude/*.class
