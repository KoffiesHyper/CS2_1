.SUFFIXES: .java .class

.java.class:
	javac $<

CLASSES = \
	MonteCarloMinimizationParallel.class \
	SearchParallel.class \
	TerrainArea.class \

default: $(CLASSES)

clean:
	rm ./*.class