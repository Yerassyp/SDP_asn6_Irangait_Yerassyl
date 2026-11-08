package filesystem;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchVisitorTest {

    @Test
    void combinesNameAndMinimumSize() {
        DirectoryNode root = new DirectoryNode("workspace");
        root.addChild(new FileNode("guide.txt", 200));

        DirectoryNode src = new DirectoryNode("src");
        src.addChild(new FileNode("App.java", 300));

        DirectoryNode utils = new DirectoryNode("utils");
        utils.addChild(new FileNode("FileHelper.java", 150));
        utils.addChild(new FileNode("AppTest.java", 90));

        src.addChild(utils);
        root.addChild(src);

        SearchVisitor visitor = new SearchVisitor("java", 150);
        visitor.execute(root);

        assertEquals(
                List.of("src/App.java", "src/utils/FileHelper.java"),
                visitor.getMatchingPaths()
        );
    }

    @Test
    void returnsSortedRootRelativePaths() {
        DirectoryNode root = new DirectoryNode("workspace");
        root.addChild(new FileNode("readme.md", 120));

        DirectoryNode docs = new DirectoryNode("docs");
        docs.addChild(new FileNode("notes.md", 80));
        root.addChild(docs);

        DirectoryNode archive = new DirectoryNode("archive");
        archive.addChild(new FileNode("notes-old.md", 60));
        root.addChild(archive);

        SearchVisitor visitor = new SearchVisitor(".md", 0);
        visitor.execute(root);

        assertEquals(
                List.of(
                        "archive/notes-old.md",
                        "docs/notes.md",
                        "readme.md"
                ),
                visitor.getMatchingPaths()
        );
    }

    @Test
    void usesSelectedSubtreeAsRoot() {
        DirectoryNode workspace = new DirectoryNode("workspace");
        DirectoryNode src = new DirectoryNode("src");
        workspace.addChild(src);

        src.addChild(new FileNode("App.java", 300));

        DirectoryNode utils = new DirectoryNode("utils");
        utils.addChild(new FileNode("FileHelper.java", 150));
        src.addChild(utils);

        SearchVisitor visitor = new SearchVisitor(".java", 0);
        visitor.execute(src);

        assertEquals(
                List.of("App.java", "utils/FileHelper.java"),
                visitor.getMatchingPaths()
        );
    }

    @Test
    void handlesEmptyDirectoryAndNoMatches() {
        SearchVisitor visitor = new SearchVisitor("java", 0);

        DirectoryNode empty = new DirectoryNode("empty");
        visitor.execute(empty);

        assertTrue(visitor.getMatchingPaths().isEmpty());

        DirectoryNode docs = new DirectoryNode("docs");
        docs.addChild(new FileNode("notes.txt", 100));

        visitor.execute(docs);

        assertTrue(visitor.getMatchingPaths().isEmpty());
    }

    @Test
    void repeatedExecutionClearsOldMatches() {
        DirectoryNode root = new DirectoryNode("workspace");
        root.addChild(new FileNode("App.java", 300));

        SearchVisitor visitor = new SearchVisitor(".java", 100);

        visitor.execute(root);
        assertEquals(List.of("App.java"), visitor.getMatchingPaths());

        visitor.execute(root);
        assertEquals(List.of("App.java"), visitor.getMatchingPaths());

        DirectoryNode empty = new DirectoryNode("empty");
        visitor.execute(empty);

        assertTrue(visitor.getMatchingPaths().isEmpty());
    }

    @Test
    void fileRootUsesItsOwnName() {
        FileNode file = new FileNode("App.java", 300);
        SearchVisitor visitor = new SearchVisitor(".java", 0);

        visitor.execute(file);

        assertEquals(List.of("App.java"), visitor.getMatchingPaths());
    }
}