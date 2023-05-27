
import resolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import { string } from "rollup-plugin-string";
import { terser } from 'rollup-plugin-terser';
import less from 'rollup-plugin-less';

// `npm run build` -> `production` is true
// `npm run dev` -> `production` is false
const production = !process.env.ROLLUP_WATCH;

export default {
	input: 'src/vue-stick.js',
	output: {
		file: 'dist/vue-stick.js',
		format: 'es',
		name: 'Stick',
		sourcemap: true
	},
	plugins: [
		less({
			insert: true,
			output: false
		}),
    string({
      // Required to be specified
      include: "**/*.html",

      // Undefined by default
      exclude: ["**/index.html"]
    }),
		resolve(), // tells Rollup how to find date-fns in node_modules
		commonjs(), // converts date-fns to ES modules
		production && terser() // minify, but only in production
	]
};