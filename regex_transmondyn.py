import re
f = open('/home/srey/TRAVAUX/THESE/REPOSITORY_GIT/simprocess-these/Library/transmondyn.bib', "r")
copy = open("/home/srey/TRAVAUX/THESE/REPOSITORY_GIT/simprocess-these/Library/transmondyn_clean_url.bib", "w")
regex = re.compile(r'(:home\/srey\/TRAVAUX\/THESE\/REPOSITORY\\_PDF\/RANGE\/)(.+)(:pdf)')
for line in f:
    line = regex.sub("http://bib.unthinkingdepths.fr/seb/pdf/\g<2>", line)
    line = line.replace("\\","")
    copy.write(line)
    print line
f.close()
copy.close()
