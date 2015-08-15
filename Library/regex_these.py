import re
import shlex, subprocess

command_line = "biber  --bblencoding=UTF-8 --O ./Library/these_bibUTF8.bib --configfile /home/srey/TRAVAUX/THESE/REPOSITORY_GIT/simprocess-these/Library/biber.conf --tool /home/srey/TRAVAUX/THESE/REPOSITORY_GIT/simprocess-these/Library/these.bib"
arg = shlex.split(command_line)

print ("BIBER Convert and remove keywords : \n ", arg)

p = subprocess.Popen(shlex.split(command_line)).wait()

print ("BIBER export to UTF8 : \n ", arg)

f = open('/home/srey/TRAVAUX/THESE/REPOSITORY_GIT/simprocess-these/Library/these_bibUTF8.bib', "r")
copy = open("/home/srey/TRAVAUX/THESE/REPOSITORY_GIT/simprocess-these/Library/these_clean_bibUTF8.bib", "w")
regex = re.compile(r'(:home\/srey\/TRAVAUX\/THESE\/REPOSITORY\\_PDF\/RANGE\/)(.+)(:pdf)')
for line in f:
    line = regex.sub("http://bib.unthinkingdepths.fr/seb/pdf/\g<2>", line)
    line = line.replace("\\","")
    copy.write(line)
f.close()
copy.close()
