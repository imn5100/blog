//package com.shaw.test;
//
//import orestes.bloomfilter.BloomFilter;
//import orestes.bloomfilter.CountingBloomFilter;
//import orestes.bloomfilter.FilterBuilder;
//
///**
// * Created by shaw on 2016/11/23 0023.
// */
//public class SimpleTest {
//    @org.junit.Test
//    public void testNormalBloomFilter() {
//        String host = "localhost";
//        int port = 6379;
//        String filterName = "normalbloomfilter:filter1";
//        //Open a Redis-backed Bloom filter
//        BloomFilter<String> bfr = new FilterBuilder(1000, 0.01)
//                .name(filterName) //use a distinct name
//                .redisBacked(true)
//                .redisHost(host) //Default is localhost
//                .redisPort(port) //Default is standard 6379
//                .buildBloomFilter();
//
//        bfr.add("cow");
//
//        //Open the same Bloom filter from anywhere else
//        BloomFilter<String> bfr2 = new FilterBuilder(1000, 0.01)
//                .name(filterName) //load the same filter
//                .redisBacked(true)
//                .buildBloomFilter();
//        bfr2.add("bison");
//
//        System.out.println(bfr.contains("cow")); //true
//        System.out.println(bfr.contains("bison")); //true
//    }
//
//    @org.junit.Test
//    public void testCountBloomFilter() {
//        //Open a Redis-backed Counting Bloom filter
//        CountingBloomFilter<String> cbfr = new FilterBuilder(10000, 0.01)
//                .name("countbloomfilter:filter1")
//                .overwriteIfExists(true) //instead of loading it, overwrite it if it's already there
//                .redisBacked(true)
//                .buildCountingBloomFilter();
//        cbfr.add("cow");
//
//        //Open a second Redis-backed Bloom filter with a new connection
//        CountingBloomFilter<String> bfr2 = new FilterBuilder(10000, 0.01)
//                .name("countbloomfilter:filter1") //this time it will be load it
//                .redisBacked(true)
//                .buildCountingBloomFilter();
//        bfr2.add("bison");
//        bfr2.remove("cow");
//
//        System.out.println(cbfr.contains("bison")); //true
//        System.out.println(cbfr.contains("cow")); //false
//    }
//}
