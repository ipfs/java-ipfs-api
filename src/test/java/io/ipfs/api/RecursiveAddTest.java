package io.ipfs.api;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

import io.ipfs.multiaddr.MultiAddress;

public class RecursiveAddTest {

    private final IPFS ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
    
    @Test
    public void testAdd() throws Exception {
        System.out.println("ipfs version: " + ipfs.version());

        String EXPECTED = "QmX5fZ6aUxNTAS7ZfYc8f4wPoMx6LctuNbMjuJZ9EmUSr6";

        Path base = Paths.get("tmpdata");
        base.toFile().mkdirs();
        Files.write(base.resolve("index.html"), "<html></html>".getBytes());
        Path js = base.resolve("js");
        js.toFile().mkdirs();
        Files.write(js.resolve("func.js"), "function() {console.log('Hey');}".getBytes());

        List<MerkleNode> add = ipfs.add(new NamedStreamable.FileWrapper(base.toFile()));
        MerkleNode node = add.get(add.size() - 1);
        Assert.assertEquals(EXPECTED, node.hash.toBase58());
    }

    @Test
    public void binaryRecursiveAdd() throws Exception {
        String EXPECTED = "Qmd1dTx4Z1PHxSHDR9jYoyLJTrYsAau7zLPE3kqo14s84d";

        Path base = Paths.get("tmpbindata");
        base.toFile().mkdirs();
        byte[] bindata = new byte[1024*1024];
        new Random(28).nextBytes(bindata);
        Files.write(base.resolve("data.bin"), bindata);
        Path js = base.resolve("js");
        js.toFile().mkdirs();
        Files.write(js.resolve("func.js"), "function() {console.log('Hey');}".getBytes());

        List<MerkleNode> add = ipfs.add(new NamedStreamable.FileWrapper(base.toFile()));
        MerkleNode node = add.get(add.size() - 1);
        Assert.assertEquals(EXPECTED, node.hash.toBase58());
    }
}