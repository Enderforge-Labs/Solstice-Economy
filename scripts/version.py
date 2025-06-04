#!/usr/bin/env python3
from ruamel.yaml import YAML
import sys

file = open(".github/workflows/publish.yml")
yaml = YAML()
workflow = yaml.load(file)
print(workflow)

env = workflow["env"]
env["VERSION"] = sys.argv[1]
env["RELEASE_NAME"] = f"Solstice Economy {sys.argv[1]}"
#print(yaml.dump(workflow,default_flow_style = False))
file = open(".github/workflows/publish.yml","w")
yaml.dump(workflow,file)
file.close()