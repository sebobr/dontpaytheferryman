package splitter;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Splitter extends Configured implements Tool {

	private static final Logger LOG = Logger.getLogger(Splitter.class);
	private static Logger SECOND_LOG;

	public Splitter(Logger lOGGER) {
		BasicConfigurator.configure();
		SECOND_LOG = lOGGER;
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Splitter(LOG), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("mapred.job.tracker", "local");
		Job job = Job.getInstance(conf, getClass().getName());
		job.setJarByClass(this.getClass());
		// Use TextInputFormat, the default unless job.setInputFormatClass is
		// used
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(SplitterMap.class);
		// job.setReducerClass(Reduce.class);
		MultipleOutputs.addNamedOutput(job, "logs", TextOutputFormat.class,
				NullWritable.class, Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(0);
		LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);

		/*
		 * Delete output filepath if already exists
		 */
		FileSystem fs = FileSystem.newInstance(getConf());

		Path outputFilePath = new Path(args[1]);
		if (fs.exists(outputFilePath)) {
			fs.delete(outputFilePath, true);
		}
		return job.waitForCompletion(true) ? 0 : 1;
	}


	public static class SplitterMap extends
			Mapper<LongWritable, Text, NullWritable, Text> {
		protected MultipleOutputs<NullWritable, Text> mos;
		// protected to allow unit testing
		protected Text word = new Text();
		protected Text path = new Text();

		@Override
		protected void setup(
				Mapper<LongWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			super.setup(context);
			mos = new MultipleOutputs<NullWritable, Text>(context);
		}

		/*
		 * Need to implement this function to close the MultipleOutputs and to
		 * make it flush its buffers to Hdfs
		 * 
		 * @see
		 * org.apache.hadoop.mapreduce.Mapper#cleanup(org.apache.hadoop.mapreduce
		 * .Mapper.Context)
		 */

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			super.cleanup(context);
			mos.close();
		}

		private long numRecords = 0;
		private static final Pattern WORD_BOUNDARY = Pattern
				.compile("\\s*\\b\\s*");
		private static final String BLOG_REGEX = "<[0-9A-Z_\\^]*>";
		private static final String Key_REGEX = "<[A-Z]*\\^[0-9]*_[0-9]*>";

		public void map(LongWritable offset, Text lineText, Context context)
				throws IOException, InterruptedException {
			String line = lineText.toString();
			String[] blocks = line.trim().replaceAll("^<", "")
					.replaceAll(">$", "").split("><");
			// for(int i =0; i< blocks.length;i++){
			// System.out.println("block "+i+": "+blocks[i]);
			// }
			String[] keys = blocks[4].trim().replaceAll("\\^([A-Za-z])", " $1")
					.split(" ");
			// for(int i =0; i< keys .length;i++){
			// System.out.println("key "+i+": "+keys [i]);
			// }
			String timestamp = blocks[0];
			int count = keys.length;
			for (int i = 0; i < count; i++) {
				String value = blocks[i + 5].substring(0,
						blocks[i + 5].length());
				String key = keys[i];
				System.out.println(key);
				String type = key.split("\\^")[0];
				String version = key.split("\\^")[1];
				String path = timestamp + "/" + type + "_" + version + ".csv";
				this.path.set(path);
				word.set(value);
				System.out.println("value " + i + ": " + value);
				mos.write("logs", NullWritable.get(), new Text(value), path);
			}
		}
	}

	// public static class Reduce extends
	// Reducer<Text, Text, Text, Text> {
	// @Override
	// public void reduce(Text key, Iterable<Text> values,
	// Context context) throws IOException, InterruptedException {
	// for (Text value : values) {
	// context.write(key, value);
	// }
	// }
	// }
}
