# Args to add when running the main
EXTRA_ARGS = --file=carte2.json
# class to run for jar and java
MAIN_CLASS = pandemic.Main
# verbose for build command
# (comment the line below to enable verbose)
V = @
# map for graphical mode
GRAPHICAL_MAP = LOTRmap.json

#################################

# source directory
SRC_DIR = src
# output directory
OUT_DIR = classes
# output directory of classes
OUT_TEST_DIR = test_classes
# output coverage (jacoco) directory
COVERAGE_DIR = coverage
# output doc directory
DOC_DIR = docs
# libraries directory
LIB_DIR = lib
# test directory
TEST_DIR = test
TEST_LIB =lib/junit-platform-console-standalone-1.4.0.jar

# sources
SRCS = $(SRC_DIR)/*.java
SOURCE_FILES := $(shell find $(SRC_DIR) -name '*.java')
CLASSES := $(SOURCE_FILES:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)

# source packages
_PACKAGE_DIR  = $(wildcard $(SRC_DIR)/*/) 
PACKAGES = $(_PACKAGE_DIR:$(SRC_DIR)/%/=%)
# test files
TEST_SOURCE_FILES := $(shell find $(TEST_DIR) -name '*.java')
TEST_CLASSES := $(TEST_SOURCE_FILES:$(TEST_DIR)/%.java=$(OUT_TEST_DIR)/%.class)


# libraries
LIBS :=$(wildcard $(LIB_DIR)/*.jar)

#########################################
# FLAGS


space = $(empty) $(empty)
join-with = $(subst $(space),$1,$(strip $2))

JARFLAGS = -cvfe Program.jar $(MAIN_CLASS)
JCFLAGS = -d $(OUT_DIR)
JVDFLAGS = -sourcepath $(SRC_DIR) -d $(DOC_DIR) -subpackages $(PACKAGES)

# os check
# because Windows uses ; instead of :
ifeq ($(OS),Windows_NT)
	LIBS_CP = $(call join-with,;,$(LIBS))
	JCPFLAGS = -cp "$(OUT_DIR);$(SRC_DIR);$(LIBS_CP)"
	JTFLAGS = -cp "$(LIBS_CP);$(OUT_DIR);$(OUT_TEST_DIR)"
	RM = rmdir /s /q
else
	LIBS_CP = $(call join-with,:,$(LIBS))
	JCPFLAGS = -cp "$(OUT_DIR):$(SRC_DIR):$(LIBS_CP)"
	JTFLAGS = -cp "$(LIBS_CP):$(OUT_DIR):$(OUT_TEST_DIR)"
	RM += -r
endif
JVDFLAGS += -cp "$(LIBS_CP)"

#########################################
# COMMANDS

JC = javac
JVM = java
JAR = jar
JVD = javadoc
UNZIP = unzip -qo

#########################################

.PHONY: build clean run jar docs test build_test all run-graphics

# default is first target found
# clean, build & run
default: build run

$(OUT_DIR)/%.class: $(SRC_DIR)/%.java
	$(V)-$(JC) $(JCFLAGS) $(JCPFLAGS) $<

# build .java into .class
build: $(CLASSES)
	@echo Compiled source files

# Clean classes/ and docs/ (not .jar)
clean: 
	@echo Cleaning files in $(OUT_DIR) and $(OUT_TEST_DIR)
	$(V)-$(RM) "$(OUT_DIR)"
	$(V)-$(RM) "$(OUT_TEST_DIR)"
	@echo Cleaning files in $(DOC_DIR)
	$(V)-$(RM) "$(DOC_DIR)"
	@echo Cleaning JaCoCo files
	$(V)-$(RM) "$(COVERAGE_DIR)"


# Run the main class with args (see head of file)
run: build
	@echo Running main class[$(MAIN_CLASS)] with args[$(EXTRA_ARGS)]
	$(V)-$(JVM) $(JCPFLAGS) $(MAIN_CLASS) $(EXTRA_ARGS)

run-graphics: build
	@echo Running main class[$(MAIN_CLASS)] with args[$(EXTRA_ARGS)]
	$(V)-$(JVM) $(JCPFLAGS) $(MAIN_CLASS) --file=$(GRAPHICAL_MAP) --graphical-mode

run-graphics-dbg: build
	$(V)-export LD_PRELOAD=/usr/lib/librenderdoc.so && \
	$(JVM) $(JCPFLAGS) $(MAIN_CLASS) --file=$(GRAPHICAL_MAP) --graphical-mode

coverage: build_test
	@echo Coverage testing...
	$(V)-$(JVM) -javaagent:$(LIB_DIR)/jacocoagent.jar=destfile=$(COVERAGE_DIR)/jacoco-coverage.exec,append=false -jar $(TEST_LIB) $(JTFLAGS) --scan-classpath --disable-banner 
	$(V)-$(JVM) -jar $(LIB_DIR)/jacococli.jar report $(COVERAGE_DIR)/jacoco-coverage.exec --sourcefiles $(SRC_DIR) --classfiles $(OUT_DIR) --html coverage
	$(V)-echo Code Coverage $$(cat $(COVERAGE_DIR)/index.html | grep -e "[0-9]*%" --only-matching | head -n1)
	@echo Open "$(COVERAGE_DIR)/index.html" to see the results

# build the jar into Program.jar
jar: Program.jar
	@echo Created [Program.jar]

Program.jar: build
	@echo Creating jar [Program.jar] 
	@echo JSON
	$(V)-$(UNZIP) lib/json.jar -d lib_tmp/
	@echo Making jar 
	$(V)-$(JAR) $(JARFLAGS) -C $(PACKAGES:%=$(OUT_DIR) %) -C lib_tmp org
	$(V)-rm -rf lib_tmp

gjar: Program_Graphics.jar
	@echo Created [Program_Graphics.jar] 

Program_Graphics.jar: build
	@echo Creating jar [Program_Graphics.jar] 
	@echo JSON
	$(V)-$(UNZIP) lib/json.jar -d lib_tmp/
	@echo JOML
	$(V)-$(UNZIP) lib/joml-1.10.5.jar -d lib_tmp/
	@echo LWGJL-ASSIMP
	$(V)-$(UNZIP) lib/lwjgl-assimp-natives-linux.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-assimp-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-assimp.jar -d lib_tmp/
	@echo LWGJL-GLFW
	$(V)-$(UNZIP) lib/lwjgl-glfw-natives-linux.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-glfw-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-glfw.jar -d lib_tmp/
	@echo LWGJL-OPENGL
	$(V)-$(UNZIP) lib/lwjgl-opengl-natives-jar.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-opengl-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-opengl.jar -d lib_tmp/
	@echo LWGJL-STB
	$(V)-$(UNZIP) lib/lwjgl-stb-natives-jar.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-stb-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-stb.jar -d lib_tmp/
	@echo LWGJL
	$(V)-$(UNZIP) lib/lwjgl-natives-jar.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl-sources.jar -d lib_tmp/
	$(V)-$(UNZIP) lib/lwjgl.jar -d lib_tmp/
	@echo Making jar 
	$(V)-$(JAR) $(JARFLAGS) -C $(PACKAGES:%=$(OUT_DIR) %) -C lib_tmp org -C lib_tmp linux
	$(V)-rm -rf lib_tmp

# build the javadoc
docs:
	@echo Creating docs in [$(DOC_DIR)] 
	$(V)-$(JVD) $(JVDFLAGS)

# run tests
$(OUT_TEST_DIR)/%.class: $(TEST_DIR)/%.java
	$(V)-$(JC) -d $(OUT_TEST_DIR) $(JTFLAGS) $<

build_test: build $(TEST_CLASSES)
	@echo Compiled test source files

test: build_test
	@echo Running test of [$(class)]
	$(V)-$(JVM) -jar $(TEST_LIB) $(JTFLAGS) --scan-classpath --disable-banner

# clean, build, build javadoc, create jar & run
all: clean build docs jar run
