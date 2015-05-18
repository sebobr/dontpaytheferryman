package splitter;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.URL;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import splitter.Splitter.SplitterMap;

public class test {

    private SplitterMap mapper;
    private Context context;
    private IntWritable one;
	final Logger LOGGER = Logger.getLogger(Splitter.class);
    private String test_text;
	
    @Before
    public void init() throws IOException, InterruptedException {
        mapper = new SplitterMap();
        context = mock(Context.class);
        mapper.word = mock(Text.class);
        mapper.mos=mock(MultipleOutputs.class);
        one = new IntWritable(1);
		BasicConfigurator.configure();
		URL url = getClass().getResource("/stat_test.txt");
		test_text = Resources.toString(url, Charsets.UTF_8);    }

//    @Test
//    public void testSingleWord() throws IOException, InterruptedException {
//        LOGGER.info(test_text);
//        mapper.map(new LongWritable(1L), new Text(test_text), context);
//        LOGGER.info(mapper.word);
//        LOGGER.info(mapper.path);
//        InOrder inOrder = inOrder(mapper.word, context);
//        assertCountedOnce(inOrder, "foo");
//    }

    @Test
	public void SplitterMaptest() throws Exception {
		
		final Logger LOGGER = Logger.getLogger(Splitter.class);
		Splitter s = new Splitter(LOGGER);
		String[] args = {getClass().getResource("/stat_test.txt").getPath(), "/tmp/test"};
		s.run(args);
	}
    private void assertCountedOnce(InOrder inOrder, String w) throws IOException, InterruptedException {
        inOrder.verify(mapper.word).set(eq(w));
        inOrder.verify(context).write(eq(mapper.word), eq(one));
      }
}
