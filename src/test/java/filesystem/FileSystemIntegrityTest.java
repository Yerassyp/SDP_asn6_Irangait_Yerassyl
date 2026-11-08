package filesystem;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemIntegrityTest {

    @Test
    void rejectsNegativeFileSize() {
        assertThrows(IllegalArgumentException.class,
                () -> new FileNode("bad.txt", -1));

        FileNode empty = new FileNode("empty.txt", 0);
        assertEquals(0L, empty.getSizeBytes());
    }

    @Test
    void rejectsDuplicateSiblingNames() {
        DirectoryNode root = new DirectoryNode("root");
        FileNode original = new FileNode("notes.md", 100);
        root.addChild(original);

        DirectoryNode duplicate = new DirectoryNode("notes.md");

        assertThrows(IllegalArgumentException.class,
                () -> root.addChild(duplicate));

        assertEquals(1, root.getChildren().size());
        assertSame(original, root.getChildren().get(0));
        assertNull(duplicate.getParent());
    }

    @Test
    void rejectsMultipleParents() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("shared.txt", 100);

        first.addChild(file);

        assertThrows(IllegalStateException.class,
                () -> first.addChild(file));

        assertThrows(IllegalStateException.class,
                () -> second.addChild(file));

        assertSame(first, file.getParent());
        assertEquals(1, first.getChildren().size());
        assertTrue(second.getChildren().isEmpty());
    }

    @Test
    void rejectsSelfContainment() {
        DirectoryNode root = new DirectoryNode("root");

        assertThrows(IllegalArgumentException.class,
                () -> root.addChild(root));

        assertTrue(root.getChildren().isEmpty());
        assertNull(root.getParent());
    }

    @Test
    void rejectsAncestorCycle() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode src = new DirectoryNode("src");
        DirectoryNode utils = new DirectoryNode("utils");

        root.addChild(src);
        src.addChild(utils);

        assertThrows(IllegalArgumentException.class,
                () -> utils.addChild(root));

        assertNull(root.getParent());
        assertSame(root, src.getParent());
        assertSame(src, utils.getParent());
        assertTrue(utils.getChildren().isEmpty());
    }

    @Test
    void rejectsNullChild() {
        DirectoryNode root = new DirectoryNode("root");

        assertThrows(NullPointerException.class,
                () -> root.addChild(null));

        assertTrue(root.getChildren().isEmpty());
    }

    @Test
    void childListCannotBeModified() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("a.txt", 100));

        List<Element> children = root.getChildren();

        assertThrows(UnsupportedOperationException.class,
                () -> children.add(new FileNode("b.txt", 200)));

        assertEquals(1, root.getChildren().size());
    }

    @Test
    void allowsSameNameInDifferentDirectories() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode docs = new DirectoryNode("docs");
        DirectoryNode archive = new DirectoryNode("archive");

        root.addChild(docs);
        root.addChild(archive);

        FileNode first = new FileNode("notes.md", 100);
        FileNode second = new FileNode("notes.md", 200);

        docs.addChild(first);
        archive.addChild(second);

        assertEquals(1, docs.getChildren().size());
        assertEquals(1, archive.getChildren().size());
        assertSame(docs, first.getParent());
        assertSame(archive, second.getParent());
    }

    @Test
    void rejectsInvalidNames() {
        assertThrows(IllegalArgumentException.class,
                () -> new DirectoryNode(""));

        assertThrows(IllegalArgumentException.class,
                () -> new DirectoryNode("   "));

        assertThrows(IllegalArgumentException.class,
                () -> new DirectoryNode("."));

        assertThrows(IllegalArgumentException.class,
                () -> new DirectoryNode(".."));

        assertThrows(IllegalArgumentException.class,
                () -> new FileNode("bad/name.txt", 100));

        assertThrows(IllegalArgumentException.class,
                () -> new FileNode("bad\\name.txt", 100));

        assertThrows(IllegalArgumentException.class,
                () -> new FileNode(null, 100));
    }
}