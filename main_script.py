from regex_these import *
import os
from distutils.file_util import *

#BasePath
projectPath = os.path.dirname(__file__)

destPath = os.path.join(projectPath,"bibliography")
originPath = os.path.dirname("/home/reyman/Documents")
originFile = os.path.join(originPath,"PHD.bib")
if os.is_file(originFile):
    os.remove(originFile)
    os.symlink(originFile,destPath)

# copy to script path

#clean_bibtex(scriptPath,"phd")