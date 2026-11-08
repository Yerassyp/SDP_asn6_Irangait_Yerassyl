package filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleDispatchTest {

    @Test
    void fileCallsFileVisit() {
        FileNode file = new FileNode("report.txt", 100);
        TrackingVisitor visitor = new TrackingVisitor();

        file.accept(visitor);

        assertEquals(1, visitor.fileVisits);
        assertEquals(0, visitor.directoryVisits);
    }

    @Test
    void directoryCallsOnlyDirectoryVisit() {
        DirectoryNode directory = new DirectoryNode("docs");
        directory.addChild(new FileNode("guide.txt", 200));

        TrackingVisitor visitor = new TrackingVisitor();

        directory.accept(visitor);

        assertEquals(0, visitor.fileVisits);
        assertEquals(1, visitor.directoryVisits);
    }

    private static class TrackingVisitor implements Visitor {

        int fileVisits;
        int directoryVisits;

        @Override
        public void visit(FileNode file) {
            fileVisits++;
        }

        @Override
        public void visit(DirectoryNode directory) {
            directoryVisits++;
        }
    }
}