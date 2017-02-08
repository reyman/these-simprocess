import re
import shlex, subprocess
import os

def clean_bibtex(basePath,bibName):

    input_file_name = os.path.join(basePath,bibName+".bib")
    conf_file_name = os.path.join(basePath, "biber.conf")
    output_utf8_file_name = os.path.join(basePath,bibName+"UTF8.bib")
    output_cleaned_file_name = os.path.join(basePath, bibName + "UTF8.bib")

    command_line = "biber  --bblencoding=UTF-8 --O %s --configfile %s --tool %s" % output_utf8_file_name,conf_file_name, input_file_name
    arg = shlex.split(command_line)
    print command_line
    print ("BIBER Convert and remove keywords : \n ", arg)

    p = subprocess.Popen(shlex.split(command_line)).wait()

    print ("BIBER export to UTF8 : \n ", arg)

    f = open(output_utf8_file_name, "r")
    copy = open(output_cleaned_file_name, "w")
    #regex = re.compile(r'(:home\/srey\/TRAVAUX\/THESE\/REPOSITORY\\_PDF\/RANGE\/)(.+)(:pdf)')
    regex = re.compile(r'(:home)(.+)(:pdf)')
    for line in f:
        line = regex.sub("http://bib.unthinkingdepths.fr/seb/pdf/\g<2>", line)
        line = line.replace("\\","")
        copy.write(line)
    f.close()
    copy.close()
